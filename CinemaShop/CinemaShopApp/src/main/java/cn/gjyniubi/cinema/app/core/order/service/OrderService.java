package cn.gjyniubi.cinema.app.core.order.service;

import cn.gjyniubi.cinema.app.core.cinema.dto.CinemaDto;
import cn.gjyniubi.cinema.app.core.cinema.mapper.FilmScheduleMapper;
import cn.gjyniubi.cinema.app.core.film.dto.FilmInfoDto;
import cn.gjyniubi.cinema.app.core.order.dto.ListOrderDto;
import cn.gjyniubi.cinema.app.core.order.dto.OrderDto;
import cn.gjyniubi.cinema.app.core.order.mapper.OrderMapper;
import cn.gjyniubi.cinema.app.core.order.vo.CreateOrderVo;
import cn.gjyniubi.cinema.app.domain.DelayMessage;
import cn.gjyniubi.cinema.app.schdule.ScheduleService;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.domain.BaseListQuery;
import cn.gjyniubi.cinema.common.domain.ListData;
import cn.gjyniubi.cinema.common.dto.ScheduleHallSite;
import cn.gjyniubi.cinema.common.entry.DocCinema;
import cn.gjyniubi.cinema.common.entry.FilmSchedule;
import cn.gjyniubi.cinema.common.entry.Order;
import cn.gjyniubi.cinema.common.entry.OrderItem;
import cn.gjyniubi.cinema.common.enums.NumberType;
import cn.gjyniubi.cinema.common.enums.OrderStatus;
import cn.gjyniubi.cinema.common.enums.SiteType;
import cn.gjyniubi.cinema.common.exception.NoSuchDataException;
import cn.gjyniubi.cinema.common.exception.VerifyException;
import cn.gjyniubi.cinema.common.mapper.*;
import cn.gjyniubi.cinema.common.service.CommonNumberService;
import cn.gjyniubi.cinema.common.util.BeanAssignment;
import cn.gjyniubi.cinema.common.util.CustomerUtil;
import cn.gjyniubi.cinema.common.util.DateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author gujianyang
 * @Date 2021/5/31
 * @Class OrderService
 */
@Slf4j
@Service
public class OrderService {

    @Autowired
    private CommonNumberService commonNumberService;

    @Autowired
    private FilmScheduleMapper filmScheduleMapper;

    @Autowired
    private Gson gson;

    @Autowired
    private CommonFilmScheduleMapper commonFilmScheduleMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CommonOrderItemMapper commonOrderItemMapper;

    @Qualifier("cus_schedule")
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CommonCinemaDocMapper commonCinemaDocMapper;

    @Autowired
    private CommonFilmDocMapper commonFilmDocMapper;

    @Autowired
    private CommonFilmTypesMapper commonFilmTypesMapper;

    @Autowired
    private CommonDocHallMapper commonDocHallMapper;

    @Transactional(rollbackFor = Exception.class)
    public String createOrder(CreateOrderVo createOrderVo) throws InterruptedException {
        if (createOrderVo.getSites().size() > 5)
            throw new VerifyException("?????????????????????????????????");
        FilmSchedule filmSchedule = commonFilmScheduleMapper.selectOne(new QueryWrapper<FilmSchedule>()
                .eq("uid", createOrderVo.getScheduleUid()));
        if (filmSchedule == null)
            throw new NoSuchDataException("?????????????????????");
        if (filmSchedule.getEndTime().before(new Date()))
            throw new VerifyException("??????????????????!");
        if (filmSchedule.getBeginTime().before(new Date()))
            throw new VerifyException("??????????????????!");
        ScheduleHallSite[][] hallSites = gson.fromJson(filmSchedule.getSiteInfo(),
                new TypeToken<ScheduleHallSite[][]>() {
                }.getType());
        List<OrderItem> items = new ArrayList<>();
        OrderItem item;
        Date createTime = new Date();
        long price = 0;
        BigDecimal hundred = new BigDecimal(100);
        for (CreateOrderVo.OrderSite site : createOrderVo.getSites()) {
            ScheduleHallSite hallSite = hallSites[site.getX()][site.getY()];
            if (hallSite.getSale())
                throw new VerifyException("??????:" + hallSite.getRow() + "???" + hallSite.getColumn() + "????????????");
            if (!hallSite.getEnable() || !SiteType.isLegalSite(hallSite.getType()))
                throw new VerifyException("??????:" + hallSite.getRow() + "???" + hallSite.getColumn() + "???????????????");
            if (hallSite.getType() == SiteType.LOVER_LEFT.getType() ||
                    hallSite.getType() == SiteType.LOVER_RIGHT.getType()) {
                //?????????
                final int col = hallSite.getType()==SiteType.LOVER_LEFT.getType()?
                        site.getY()+1:site.getY()-1;
                //????????????col???row????????? ?????????????????????
                if (!siteInList(site.getX(),col,createOrderVo.getSites())) {
                    throw new VerifyException("???????????????????????????");
                }
            }
            item=new OrderItem();
            item.setCol(hallSite.getColumn());//??????row??????
            item.setRow(hallSite.getRow());//??????col??????
            item.setPrice(new BigDecimal(hallSite.getPrice()).multiply(hundred).longValue());
            item.setType(hallSite.getType());
            item.setCreateTime(createTime);
            item.setX(site.getX());// ??????row
            item.setY(site.getY());// ??????col
            items.add(item);
            price+=item.getPrice();
            hallSite.setSale(true);//?????????
        }
        Order order = new Order();
        order.setCommonStatus(UserContact.NOT_COMMENT);
        order.setCreateTime(createTime);
        order.setPhone(createOrderVo.getPhone());
        order.setScheduleId(filmSchedule.getId());
        order.setStatus(OrderStatus.WAIT_PAY.getCode());
        order.setPrice(price);
        String num = commonNumberService.generateNumber(NumberType.ORDER,3);
        if(num==null)
            throw new VerifyException("??????????????????,?????????");
        order.setOrderNum(DateTimeUtil.getOrderDate()+num);
        order.setCusId(CustomerUtil.getCurrentCustomer().getId());
        orderMapper.insert(order);
        items.forEach(orderItem -> {
            orderItem.setOrderId(order.getId());
            commonOrderItemMapper.insert(orderItem);
        });
        //?????????
        final int result = commonFilmScheduleMapper.update(null, new UpdateWrapper<FilmSchedule>()
                .eq("id", filmSchedule.getId())
                .eq("version", filmSchedule.getVersion())
                .set("version",filmSchedule.getVersion()+1)
                .set("site_info", gson.toJson(hallSites)));
        if(result==0)
            throw new VerifyException("?????????????????????????????????");
        //15????????????
        scheduleService.addNewDelayMessage(DelayMessage.builder()
                .data(order)
                .begin(order.getCreateTime().getTime())
                .delay(15)
                .delayUnit(TimeUnit.MINUTES)
                .build());
        return order.getOrderNum();
    }


    private boolean siteInList(int row, int col, @NotEmpty List<CreateOrderVo.OrderSite> sites){
        for (int i = 0; i < sites.size(); i++) {
            if(sites.get(i).getX()==row&&sites.get(i).getY()==col)
                return true;
        }
        return false;
    }

    public OrderDto getOrderInfo(String orderNum) {
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("order_num",orderNum));
        if(order==null)
            throw new NoSuchDataException("???????????????");
        FilmSchedule filmSchedule = commonFilmScheduleMapper.selectByIdWithAllLogicStatus(order.getScheduleId());
        DocCinema cinema = commonCinemaDocMapper.selectByIdWithAllLogicStatus(filmSchedule.getCinemaId());
        CinemaDto cinemaDto = BeanAssignment.copySameFields(cinema, new CinemaDto());
        cinemaDto.setFullPosition(cinema.getPosition()+" "+cinema.getAddress());
        FilmInfoDto filmInfoDto = BeanAssignment.copySameFields(commonFilmDocMapper
                .selectByIdWithAllLogicStatus(filmSchedule.getFilmId()),new FilmInfoDto());
        filmInfoDto.setTypes(commonFilmTypesMapper.selectFilmSimpleTypes(filmSchedule.getFilmId()));
        return OrderDto.builder()
                .order(order)
                .items(commonOrderItemMapper.selectList(new QueryWrapper<OrderItem>()
                        .eq("order_id",order.getId())))
                .cinemaDto(cinemaDto)
                .filmInfoDto(filmInfoDto)
                .begin(filmSchedule.getBeginTime())
                .end(filmSchedule.getEndTime())
                .hallName(commonDocHallMapper.selectByIdWithAllLogicStatus(filmSchedule.getHallId()).getName())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void doOrderTimeOut(Order order) {
        int col = orderMapper.update(null, new UpdateWrapper<Order>()
                .set("`status`", OrderStatus.TIMEOUT.getCode())
                .eq("`status`", OrderStatus.WAIT_PAY.getCode())
                .eq("id", order.getId()));
        log.info("????????????: ??????:{},??????:{},??????????????????:{}",order.getId(),order.getOrderNum(),col>0);
        if(col==1){
            log.info("??????????????????:{}????????????",order.getOrderNum());
            List<OrderItem> items = commonOrderItemMapper.selectList(new QueryWrapper<OrderItem>()
                    .eq("order_id",order.getId()));
            FilmSchedule schedule = filmScheduleMapper.selectById(order.getScheduleId());
            if (schedule==null)
                return;
            ScheduleHallSite[][] hallSites = gson.fromJson(schedule.getSiteInfo(),
                    new TypeToken<ScheduleHallSite[][]>() {
                    }.getType());
            for (OrderItem item : items) {
                if(!hallSites[item.getX()][item.getY()].getSale()){
                    log.error("?????????????????????:{}",item);
                    throw new VerifyException("????????????????????????");
                }
                hallSites[item.getX()][item.getY()].setSale(false);
            }
            int result = commonFilmScheduleMapper.update(null, new UpdateWrapper<FilmSchedule>()
                    .eq("id", schedule.getId())
                    .eq("version", schedule.getVersion())
                    .set("version",schedule.getVersion()+1)
                    .set("site_info", gson.toJson(hallSites)));
            //??????????????????????????????
            if(result==0){
                //???????????????????????????
                scheduleService.addNewDelayMessage(DelayMessage.builder()
                        .data(order)
                        .begin(System.currentTimeMillis())
                        .delay(1)
                        .delayUnit(TimeUnit.SECONDS)
                        .build());
                log.info("??????????????????:{}??????????????????",order.getOrderNum());
                throw new VerifyException("?????????????????????????????????");
            }
            log.info("??????????????????:{}??????????????????",order.getOrderNum());
        }else {
            log.error("????????????????????????");
        }
    }

    public void pay(String orderNum) {
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("order_num", orderNum));
        if(order==null)
            throw new NoSuchDataException("??????????????????!");
        final int col = orderMapper.update(null,
                new UpdateWrapper<Order>().set("`status`", OrderStatus.HAS_PAY.getCode())
                        .set("pay_time",new Date())
                        .eq("id", order.getId())
                        .eq("`status`", OrderStatus.WAIT_PAY.getCode()));
        if(col==0)
            throw new VerifyException("????????????????????????????????????????????????");
    }

    public ListData<ListOrderDto> getOrderList(BaseListQuery query) {
        return ListData.buildListData(
                orderMapper.selectOrderList(query,CustomerUtil.getCurrentCustomer().getId()),
                ListOrderDto::getId,true
        );
    }

//    public static void main(String[] args) {
//        final String json = "[[{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":1,\"column\":1,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":1,\"column\":2,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":1,\"column\":3,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":1,\"column\":4,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":1,\"column\":5,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"sale\":false,\"enable\":true,\"type\":-1},{\"sale\":false,\"enable\":true,\"type\":-1},{\"sale\":false,\"enable\":true,\"type\":-1},{\"sale\":false,\"enable\":true,\"type\":-1},{\"sale\":false,\"enable\":true,\"type\":-1},{\"sale\":false,\"enable\":true,\"type\":-1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":1,\"column\":6,\"type\":1},{\"price\":\"19.9\",\"sale\":false,\"enable\":true,\"row\":1,\"column\":7,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":1,\"column\":8,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":1,\"column\":9,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":1,\"column\":10,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":1,\"column\":11,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":1,\"column\":12,\"type\":1}],[{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":1,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":2,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":3,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":4,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":5,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":6,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":7,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":8,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":9,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":10,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":11,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":12,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":13,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":14,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":15,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":16,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":17,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":2,\"column\":18,\"type\":1}],[{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":1,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":2,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":3,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":4,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":5,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":6,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":7,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":8,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":9,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":10,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":11,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":12,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":13,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":14,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":15,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":16,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":17,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":3,\"column\":18,\"type\":1}],[{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":1,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":2,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":3,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":4,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":5,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":6,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":7,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":8,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":9,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":10,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":11,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":12,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":13,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":14,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":15,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":16,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":17,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":4,\"column\":18,\"type\":1}],[{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":1,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":2,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":3,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":4,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":5,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":6,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":7,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":8,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":9,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":10,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":11,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":12,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":13,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":14,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":15,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":16,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":17,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":5,\"column\":18,\"type\":1}],[{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":1,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":2,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":3,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":4,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":5,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":6,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":7,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":8,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":9,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":10,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":11,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":12,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":13,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":14,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":15,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":16,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":17,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":6,\"column\":18,\"type\":1}],[{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":1,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":2,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":3,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":4,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":5,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":6,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":7,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":8,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":9,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":10,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":11,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":12,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":13,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":14,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":15,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":16,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":17,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":7,\"column\":18,\"type\":1}],[{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":1,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":2,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":3,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":4,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":5,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":6,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":7,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":8,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":9,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":10,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":11,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":12,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":13,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":14,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":15,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":16,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":17,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":8,\"column\":18,\"type\":1}],[{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":1,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":2,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":3,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":4,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":5,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":6,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":7,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":8,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":9,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":10,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":11,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":12,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":13,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":14,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":15,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":16,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":17,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":9,\"column\":18,\"type\":1}],[{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":1,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":2,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":3,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":4,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":5,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":6,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":7,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":8,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":9,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":10,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":11,\"type\":1},{\"sale\":false,\"enable\":true,\"type\":0},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":12,\"type\":1},{\"price\":\"23\",\"sale\":true,\"enable\":true,\"row\":10,\"column\":13,\"type\":1},{\"price\":\"23\",\"sale\":true,\"enable\":true,\"row\":10,\"column\":14,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":15,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":16,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":17,\"type\":1},{\"price\":\"23\",\"sale\":false,\"enable\":true,\"row\":10,\"column\":18,\"type\":1}]]";
//        ScheduleHallSite[][] hallSites = new Gson().fromJson(json,
//                new TypeToken<ScheduleHallSite[][]>() {
//                }.getType());
//        for (int i = 0; i < hallSites.length; i++) {
//            for (int j = 0; j < hallSites[i].length; j++) {
//                if(hallSites[i][j].getSale()){
//                    System.out.println(i + " " + j);
//                }
//            }
//        }
//    }
}
