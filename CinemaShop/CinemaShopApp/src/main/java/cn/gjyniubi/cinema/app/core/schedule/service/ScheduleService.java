package cn.gjyniubi.cinema.app.core.schedule.service;

import cn.gjyniubi.cinema.app.core.cinema.dto.CinemaDto;
import cn.gjyniubi.cinema.app.core.film.dto.FilmInfoDto;
import cn.gjyniubi.cinema.app.core.schedule.dto.ScheduleInfoDto;
import cn.gjyniubi.cinema.common.dto.ScheduleHallSite;
import cn.gjyniubi.cinema.common.entry.DocCinema;
import cn.gjyniubi.cinema.common.entry.FilmSchedule;
import cn.gjyniubi.cinema.common.exception.NoSuchDataException;
import cn.gjyniubi.cinema.common.mapper.*;
import cn.gjyniubi.cinema.common.util.BeanAssignment;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author gujianyang
 * @Date 2021/5/29
 * @Class ScheduleService
 */
@Service
public class ScheduleService {

    @Autowired
    private CommonFilmScheduleMapper commonFilmScheduleMapper;

    @Autowired
    private CommonDocHallMapper commonDocHallMapper;

    @Autowired
    private CommonDocFilmTypeMapper commonDocFilmTypeMapper;

    @Autowired
    private CommonFilmDocMapper commonFilmDocMapper;

    @Autowired
    private CommonFilmTypesMapper commonFilmTypesMapper;

    @Autowired
    private CommonCinemaDocMapper commonCinemaDocMapper;

    @Autowired
    private Gson gson;

    public ScheduleHallSite[][] getHallSites(String uid) {
        FilmSchedule filmSchedule = commonFilmScheduleMapper.selectOne(new QueryWrapper<FilmSchedule>().eq("uid", uid));
        if(filmSchedule==null)
            throw new NoSuchDataException("没有此排片记录");
        return gson.fromJson(filmSchedule.getSiteInfo(), new TypeToken<ScheduleHallSite[][]>(){}.getType());
    }

    public ScheduleInfoDto getScheduleInfo(String uid) {
        FilmSchedule filmSchedule = commonFilmScheduleMapper.selectOne(new QueryWrapper<FilmSchedule>().eq("uid", uid));
        if(filmSchedule==null)
            throw new NoSuchDataException("没有此排片记录");
        FilmInfoDto filmInfoDto = BeanAssignment.copySameFields(commonFilmDocMapper
                        .selectById(filmSchedule.getFilmId())
                ,new FilmInfoDto());
        filmInfoDto.setTypes(commonFilmTypesMapper.selectFilmSimpleTypes(filmSchedule.getFilmId()));
        DocCinema cinema = commonCinemaDocMapper.selectById(filmSchedule.getCinemaId());
        CinemaDto cinemaDto = BeanAssignment.copySameFields(cinema, new CinemaDto());
        cinemaDto.setFullPosition(cinema.getPosition()+" "+cinema.getAddress());
        return ScheduleInfoDto.builder()
                .begin(filmSchedule.getBeginTime())
                .end(filmSchedule.getEndTime())
                .filmInfoDto(filmInfoDto)
                .hallName(commonDocHallMapper.selectById(filmSchedule.getHallId()).getName())
                .cinemaDto(cinemaDto)
                .build();
    }
}
