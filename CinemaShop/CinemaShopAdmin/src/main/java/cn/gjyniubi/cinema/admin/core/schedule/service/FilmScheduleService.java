package cn.gjyniubi.cinema.admin.core.schedule.service;

import cn.gjyniubi.cinema.admin.core.doc.cinema.mapper.CinemaMapper;
import cn.gjyniubi.cinema.admin.core.doc.film.mapper.FilmDocMapper;
import cn.gjyniubi.cinema.admin.core.doc.hall.mapper.HallMapper;
import cn.gjyniubi.cinema.admin.core.order.list.mapper.OrderMapper;
import cn.gjyniubi.cinema.admin.core.schedule.dto.FilmScheduleDto;
import cn.gjyniubi.cinema.admin.core.schedule.mapper.FilmScheduleMapper;
import cn.gjyniubi.cinema.admin.core.schedule.vo.CreateScheduleVo;
import cn.gjyniubi.cinema.admin.core.schedule.vo.QueryScheduleFilmVo;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.domain.HallSite;
import cn.gjyniubi.cinema.common.domain.JsonResult;
import cn.gjyniubi.cinema.common.domain.TableData;
import cn.gjyniubi.cinema.common.dto.ScheduleHallSite;
import cn.gjyniubi.cinema.common.entry.*;
import cn.gjyniubi.cinema.common.enums.OrderStatus;
import cn.gjyniubi.cinema.common.enums.SiteType;
import cn.gjyniubi.cinema.common.exception.NoSuchDataException;
import cn.gjyniubi.cinema.common.util.BeanAssignment;
import cn.gjyniubi.cinema.common.util.HttpRequestUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * @Author gujianyang
 * @Date 2021/5/20
 * @Class FilmScheduleService
 */
@Service
public class FilmScheduleService {

    @Autowired
    private FilmScheduleMapper filmScheduleMapper;

    @Autowired
    private CinemaMapper cinemaMapper;

    @Autowired
    private HallMapper hallMapper;

    @Autowired
    private FilmDocMapper filmDocMapper;

    @Autowired
    private Gson gson;

    @Autowired
    private OrderMapper orderMapper;

    // TODO: 2021/5/20 ??????????????????
    public TableData<FilmScheduleDto> getFilmScheduleList(QueryScheduleFilmVo queryScheduleFilmVo) {
        return TableData.buildData(
                filmScheduleMapper.selectFilmScheduleList(queryScheduleFilmVo),
                filmScheduleMapper.selectFilmScheduleCount(queryScheduleFilmVo)
        );
    }

    public ScheduleHallSite[][] getSite(Integer id) {
        FilmSchedule filmSchedule = filmScheduleMapper.selectById(id);
        if(filmSchedule==null)
            throw new NoSuchDataException("??????????????????!");
        return gson.fromJson(filmSchedule.getSiteInfo(),
                new TypeToken<ScheduleHallSite[][]>(){}.getType());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteFilmSchedule(Integer id) {
        FilmSchedule schedule = filmScheduleMapper.selectById(id);
        if(schedule==null)
            throw new NoSuchDataException("???????????????!");
        // ??????????????????????????????????????????????????????
        if(schedule.getEndTime().after(new Date())){
            orderMapper.update(null,new UpdateWrapper<Order>()
                    .set("`status`", OrderStatus.RETURN.getCode())
                    .eq("`status`",OrderStatus.HAS_PAY.getCode())
                    .eq("schedule_id",id));
            // TODO: 2021/6/1 ???????????????
        }
        filmScheduleMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public JsonResult<Void> createSchedule(CreateScheduleVo createScheduleVo) {
        JsonResult<Void> checkData = checkCreateData(createScheduleVo);
        if(checkData!=null)
            return checkData;
        FilmSchedule filmSchedule = BeanAssignment.copySameFields(createScheduleVo,new FilmSchedule());
        filmSchedule.setLogicDel(UserContact.LOGIC_DEL_NO_DELETE);
        filmSchedule.setCreateBy(HttpRequestUtil.getSysUserId());
        filmSchedule.setCreateTime(new Date());
        filmSchedule.setSiteInfo(gson.toJson(createScheduleVo.getSites()));
        filmSchedule.setTotalSite((int) Arrays.stream(createScheduleVo.getSites())
                .flatMap(Arrays::stream)
                .filter(item->SiteType.isLegalSite(item.getType())&&item.getEnable())
                .count());
        filmSchedule.setMinPrice(createScheduleVo.getMinPrice());
        filmSchedule.setUid(UUID.randomUUID().toString());
        filmSchedule.setVersion(UserContact.MIN_VERSION);
        filmScheduleMapper.insert(filmSchedule);
        return JsonResult.buildSuccess(null);
    }

    private JsonResult<Void> checkCreateData(CreateScheduleVo createScheduleVo){
        if(createScheduleVo.getSites()==null||createScheduleVo.getSites().length==0
                ||createScheduleVo.getSites()[0].length==0){
            return JsonResult.buildFail("??????????????????!");
        }
        System.out.println(createScheduleVo.getBeginTime());
        if(createScheduleVo.getBeginTime().before(new Date()))
            return JsonResult.buildFail("??????????????????????????????????????????");
        if(createScheduleVo.getBeginTime().after(createScheduleVo.getEndTime()))
            return JsonResult.buildFail("????????????????????????????????????????????????");
        DocCinema cinema = cinemaMapper.selectById(createScheduleVo.getCinemaId());
        if(cinema==null)
            return JsonResult.buildFail("????????????????????????");
        DocHall hall = hallMapper.selectById(createScheduleVo.getHallId());
        if(hall==null)
            return JsonResult.buildFail("??????????????????!");
        if(!hall.getCinemaId().equals(cinema.getId()))
            return JsonResult.buildFail("?????????????????????????????????!");
        if(hall.getSites()==null)
            return JsonResult.buildFail("??????????????????????????????!");
        DocFilm film = filmDocMapper.selectById(createScheduleVo.getFilmId());
        if(film==null)
            return JsonResult.buildFail("??????????????????!");
        if(film.getStatus()!= UserContact.STATUS_ACTIVE)
            return JsonResult.buildFail("???????????????????????????!");
        if(cinema.getStatus()!=UserContact.STATUS_ACTIVE)
            return JsonResult.buildFail("????????????????????????");
        if(hall.getStatus()!=UserContact.STATUS_ACTIVE)
            return JsonResult.buildFail("????????????????????????");
        HallSite[][] hallSites=gson.fromJson(hall.getSites(),new TypeToken<HallSite[][]>(){}.getType());
        if(hallSites.length!=createScheduleVo.getSites().length)
            return JsonResult.buildFail("???????????????????????????????????????????????????????????????");
        ScheduleHallSite site;
        BigDecimal price;
        BigDecimal min = null;
        for(int i=0,l=hallSites.length;i<l;i++){
            if(hallSites[i].length!=createScheduleVo.getSites()[i].length){
                return JsonResult.buildFail("???????????????????????????????????????????????????????????????");
            }
            int col = 1;
            for(int j=0,ll=hallSites[i].length;j<ll;j++){
                site = createScheduleVo.getSites()[i][j];
                if(!hallSites[i][j].getType().equals(site.getType()))
                    return JsonResult.buildFail("???????????????????????????????????????????????????????????????");
                if(!SiteType.isLegalSite(site.getType())||!site.getEnable())
                    continue;
                if(site.getPrice()==null)
                    return JsonResult.buildFail("???"+(i+1)+"?????????"+(j+1)+"???????????????!");
                try {
                    price=new BigDecimal(site.getPrice());
                    if(price.compareTo(BigDecimal.ZERO)<1)
                        return JsonResult.buildFail("???"+(i+1)+"?????????"+(j+1)+"??????????????????????????????0!");
                    //????????????
                    if(price.scale()>2)
                        site.setPrice(price.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    if(min==null)
                        min = price;
                    else if(min.compareTo(price)>0)
                        min = price;
                    site.setRow(i+1);
                    site.setColumn(col++);
                    if(site.getSale()==null)
                        site.setSale(false);
                }catch (NumberFormatException e) {
                    return JsonResult.buildFail("???"+(i+1)+"?????????"+(j+1)+"???????????????!");
                }
            }
        }
        assert min != null;
        createScheduleVo.setMinPrice(min.toString());
        return null;
    }

}
