package cn.gjyniubi.cinema.admin.core.schedule.controller;

import cn.gjyniubi.cinema.common.dto.ScheduleHallSite;
import cn.gjyniubi.cinema.admin.core.schedule.dto.FilmScheduleDto;
import cn.gjyniubi.cinema.admin.core.schedule.service.FilmScheduleService;
import cn.gjyniubi.cinema.admin.core.schedule.vo.CreateScheduleVo;
import cn.gjyniubi.cinema.admin.core.schedule.vo.QueryScheduleFilmVo;
import cn.gjyniubi.cinema.common.domain.JsonResult;
import cn.gjyniubi.cinema.common.domain.TableData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author gujianyang
 * @Date 2021/5/20
 * @Class FilmScheduleController
 */
@CrossOrigin
@Api(tags = "电影排片接口")
@RequestMapping("/schedule/film")
@RestController
public class FilmScheduleController {

    @Autowired
    private FilmScheduleService filmScheduleService;

    @ApiOperation("获取排片列表")
    @GetMapping("/list")
    public TableData<FilmScheduleDto> getFilmScheduleList(@Validated QueryScheduleFilmVo queryScheduleFilmVo){
        return filmScheduleService.getFilmScheduleList(queryScheduleFilmVo);
    }

    @ApiOperation("获取座位信息")
    @GetMapping("/site/get")
    public ScheduleHallSite[][] getSite(@RequestParam Integer id){
        return filmScheduleService.getSite(id);
    }

    @ApiOperation("删除排片")
    @PostMapping("/delete")
    public void delete(@RequestParam Integer id){
        filmScheduleService.deleteFilmSchedule(id);
    }

    @ApiOperation("创建新的排片")
    @PostMapping("/create")
    public JsonResult<Void> create(@Validated @RequestBody CreateScheduleVo createScheduleVo){
       return filmScheduleService.createSchedule(createScheduleVo);
    }

}
