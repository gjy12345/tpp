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
            throw new VerifyException("一次最多只能购买五张票");
        FilmSchedule filmSchedule = commonFilmScheduleMapper.selectOne(new QueryWrapper<FilmSchedule>()
                .eq("uid", createOrderVo.getScheduleUid()));
        if (filmSchedule == null)
            throw new NoSuchDataException("没有此排片记录");
        if (filmSchedule.getEndTime().before(new Date()))
            throw new VerifyException("此电影已结束!");
        if (filmSchedule.getBeginTime().before(new Date()))
            throw new VerifyException("此电影已开场!");
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
                throw new VerifyException("座位:" + hallSite.getRow() + "排" + hallSite.getColumn() + "列已售出");
            if (!hallSite.getEnable() || !SiteType.isLegalSite(hallSite.getType()))
                throw new VerifyException("座位:" + hallSite.getRow() + "排" + hallSite.getColumn() + "列不可购买");
            if (hallSite.getType() == SiteType.LOVER_LEFT.getType() ||
                    hallSite.getType() == SiteType.LOVER_RIGHT.getType()) {
                //情侣座
                final int col = hallSite.getType()==SiteType.LOVER_LEFT.getType()?
                        site.getY()+1:site.getY()-1;
                //前端上传col和row为下标 而非座位的序号
                if (!siteInList(site.getX(),col,createOrderVo.getSites())) {
                    throw new VerifyException("情侣座必须同时购买");
                }
            }
            item=new OrderItem();
            item.setCol(hallSite.getColumn());//座位row编号
            item.setRow(hallSite.getRow());//座位col编号
            item.setPrice(new BigDecimal(hallSite.getPrice()).multiply(hundred).longValue());
            item.setType(hallSite.getType());
            item.setCreateTime(createTime);
            item.setX(site.getX());// 座位row
            item.setY(site.getY());// 座位col
            items.add(item);
            price+=item.getPrice();
            hallSite.setSale(true);//锁座位
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
            throw new VerifyException("创建订单失败,请重试");
        order.setOrderNum(DateTimeUtil.getOrderDate()+num);
        order.setCusId(CustomerUtil.getCurrentCustomer().getId());
        orderMapper.insert(order);
        items.forEach(orderItem -> {
            orderItem.setOrderId(order.getId());
            commonOrderItemMapper.insert(orderItem);
        });
        //乐观锁
        final int result = commonFilmScheduleMapper.update(null, new UpdateWrapper<FilmSchedule>()
                .eq("id", filmSchedule.getId())
                .eq("version", filmSchedule.getVersion())
                .set("version",filmSchedule.getVersion()+1)
                .set("site_info", gson.toJson(hallSites)));
        if(result==0)
            throw new VerifyException("下单失败，请重新尝试！");
        //15分钟有效
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
            throw new NoSuchDataException("没有此订单");
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
        log.info("订单过期: 编号:{},单号:{},是否处理成功:{}",order.getId(),order.getOrderNum(),col>0);
        if(col==1){
            log.info("为订单编号为:{}释放座位",order.getOrderNum());
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
                    log.error("错误的座位状态:{}",item);
                    throw new VerifyException("座位状态不为售卖");
                }
                hallSites[item.getX()][item.getY()].setSale(false);
            }
            int result = commonFilmScheduleMapper.update(null, new UpdateWrapper<FilmSchedule>()
                    .eq("id", schedule.getId())
                    .eq("version", schedule.getVersion())
                    .set("version",schedule.getVersion()+1)
                    .set("site_info", gson.toJson(hallSites)));
            //有其他线程修改了座位
            if(result==0){
                //一秒后重试释放座位
                scheduleService.addNewDelayMessage(DelayMessage.builder()
                        .data(order)
                        .begin(System.currentTimeMillis())
                        .delay(1)
                        .delayUnit(TimeUnit.SECONDS)
                        .build());
                log.info("为订单编号为:{}释放座位失败",order.getOrderNum());
                throw new VerifyException("释放失败，准备重新尝试");
            }
            log.info("为订单编号为:{}释放座位成功",order.getOrderNum());
        }else {
            log.error("订单更新状态失败");
        }
    }

    public void pay(String orderNum) {
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("order_num", orderNum));
        if(order==null)
            throw new NoSuchDataException("此订单不存在!");
        final int col = orderMapper.update(null,
                new UpdateWrapper<Order>().set("`status`", OrderStatus.HAS_PAY.getCode())
                        .set("pay_time",new Date())
                        .eq("id", order.getId())
                        .eq("`status`", OrderStatus.WAIT_PAY.getCode()));
        if(col==0)
            throw new VerifyException("支付失败，此订单已过期或已被支付");
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
