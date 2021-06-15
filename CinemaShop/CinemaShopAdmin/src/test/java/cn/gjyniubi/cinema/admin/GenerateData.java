package cn.gjyniubi.cinema.admin;

import cn.gjyniubi.cinema.admin.core.doc.cinema.mapper.CinemaMapper;
import cn.gjyniubi.cinema.admin.core.doc.film.mapper.FilmDocMapper;
import cn.gjyniubi.cinema.admin.core.doc.hall.mapper.HallMapper;
import cn.gjyniubi.cinema.admin.core.schedule.mapper.FilmScheduleMapper;
import cn.gjyniubi.cinema.admin.core.schedule.service.FilmScheduleService;
import cn.gjyniubi.cinema.admin.core.schedule.vo.CreateScheduleVo;
import cn.gjyniubi.cinema.common.domain.HallSite;
import cn.gjyniubi.cinema.common.domain.JsonResult;
import cn.gjyniubi.cinema.common.dto.ScheduleHallSite;
import cn.gjyniubi.cinema.common.entry.DocCinema;
import cn.gjyniubi.cinema.common.entry.DocFilm;
import cn.gjyniubi.cinema.common.entry.DocHall;
import cn.gjyniubi.cinema.common.enums.JsonResultType;
import cn.gjyniubi.cinema.common.exception.VerifyException;
import cn.gjyniubi.cinema.common.mapper.CommonUserMapper;
import cn.gjyniubi.cinema.common.util.BeanAssignment;
import cn.gjyniubi.cinema.common.util.DateTimeUtil;
import cn.gjyniubi.cinema.common.util.HttpRequestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * @Author gujianyang
 * @Date 2021/5/25
 * @Class GenerateData
 */
@Slf4j
@SpringBootTest
public class GenerateData {

    @Autowired
    private cn.gjyniubi.cinema.admin.util.GenerateData generateData;

    @Autowired
    private FilmScheduleMapper filmScheduleMapper;

    @Autowired
    private FilmScheduleService filmScheduleService;

    @Autowired
    private FilmDocMapper filmDocMapper;

    @Autowired
    private HallMapper hallMapper;

    @Autowired
    private CinemaMapper cinemaMapper;

    @Autowired
    private CommonUserMapper commonUserMapper;

    @Test
    public void generate() throws Exception {
        generateData.generate();
    }

    @Rollback(false)
    @Transactional(rollbackFor = Exception.class)
    @Test
    public void generateSchedule(){
        LocalDateTime localDateTime = LocalDateTime.of(2021,6,7,0,0);
        LocalDateTime stop = LocalDateTime.of(2021,6,12,0,0);
        List<DocFilm> docFilms = filmDocMapper.selectPage(new Page<>(1,10),
                new QueryWrapper<DocFilm>().orderByDesc("weight")).getRecords();
        List<DocCinema> cinemas = cinemaMapper.selectList(null);
        CreateScheduleVo createScheduleVo;
        Gson gson = new Gson();
        HttpRequestUtil.setSysUser(commonUserMapper.selectById(1));
        while (true){
            for (DocCinema cinema : cinemas) {
                createScheduleVo=new CreateScheduleVo();
                createScheduleVo.setCinemaId(cinema.getId());
                createScheduleVo.setBeginTime(DateTimeUtil.localDateTimeToDate(localDateTime));
                final LocalDateTime end = localDateTime.plusHours(2);
                createScheduleVo.setEndTime(DateTimeUtil.localDateTimeToDate(end));
                createScheduleVo.setFilmId(docFilms.get(new Random().nextInt(docFilms.size())).getId());
                List<DocHall> halls=hallMapper.selectList(
                        new QueryWrapper<DocHall>()
                                .eq("cinema_id",cinema.getId())
                                .isNotNull("sites"));
                for (DocHall hall : halls) {
                    createScheduleVo.setHallId(hall.getId());
                    createScheduleVo.setSites(transferHallSite(gson.fromJson(hall.getSites(),
                            new TypeToken<HallSite[][]>(){}.getType())));
                    JsonResult<?> result = filmScheduleService.createSchedule(createScheduleVo);
                    if(result.getCode()!= JsonResultType.SUCCESS.getCode()){
                        log.error(result.getMsg());
                        throw new VerifyException("创建失败!");
                    }
                    log.info("创建排片--"+result.getCode());
                }
            }
            localDateTime = localDateTime.plusHours(3);
            if(localDateTime.isAfter(stop))
                break;
        }
    }

    private ScheduleHallSite[][] transferHallSite(HallSite[][] halls){
        ScheduleHallSite[][] scheduleHallSites = new ScheduleHallSite[halls.length][];
        for (int i = 0; i < scheduleHallSites.length; i++) {
            ScheduleHallSite[] row = new ScheduleHallSite[halls[i].length];
            for (int j = 0; j < row.length; j++) {
                row[j] = BeanAssignment.copySameFields(halls[i][j],new ScheduleHallSite());
                row[j].setEnable(true);
                row[j].setPrice(String.valueOf(new Random().nextInt(50)+10));
            }
            scheduleHallSites[i] = row;
        }
        return scheduleHallSites;
    }

}
