package cn.gjyniubi.cinema.app.core.cinema.controller;

import cn.gjyniubi.cinema.app.core.cinema.dto.CinemaDto;
import cn.gjyniubi.cinema.app.core.cinema.dto.FilmScheduleDto;
import cn.gjyniubi.cinema.app.core.cinema.dto.SimpleCinemaDto;
import cn.gjyniubi.cinema.app.core.cinema.service.CinemaService;
import cn.gjyniubi.cinema.app.core.cinema.vo.CinemaListQuery;
import cn.gjyniubi.cinema.app.core.cinema.vo.ScheduleCinemaListQuery;
import cn.gjyniubi.cinema.common.domain.ListData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/26
 * @Class CinemaController
 */
@Api(tags = "影院页面")
@CrossOrigin
@RequestMapping("/cinema")
@RestController
public class CinemaController {

    @Autowired
    private CinemaService cinemaService;

    @ApiOperation("获取电影院列表")
    @GetMapping("/list")
    public ListData<SimpleCinemaDto> getCinemaList(@Validated CinemaListQuery query){
        return cinemaService.getCinemaList(query);
    }

    @ApiOperation("获取有此电影排片计划的电影院")
    @GetMapping("/hasScheduleCinemaList")
    public ListData<SimpleCinemaDto> getHasScheduleCinemaList(@Validated ScheduleCinemaListQuery query){
        return cinemaService.getHasScheduleCinemaList(query);
    }

    @ApiOperation("获取影院七天的电影排片")
    @GetMapping("scheduleFilms")
    public List<FilmScheduleDto> getScheduleFilms(@RequestParam String cinemaUid){
        return cinemaService.getScheduleFilms(cinemaUid);
    }

    @ApiOperation("获取影院详情")
    @GetMapping("/info")
    public CinemaDto getCinemaInfo(@RequestParam String uid){
        return cinemaService.getCinemaInfo(uid);
    }

}
