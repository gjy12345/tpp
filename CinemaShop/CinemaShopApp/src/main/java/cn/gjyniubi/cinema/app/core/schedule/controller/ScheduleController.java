package cn.gjyniubi.cinema.app.core.schedule.controller;

import cn.gjyniubi.cinema.app.core.schedule.dto.ScheduleInfoDto;
import cn.gjyniubi.cinema.app.core.schedule.service.ScheduleService;
import cn.gjyniubi.cinema.common.dto.ScheduleHallSite;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * @Author gujianyang
 * @Date 2021/5/29
 * @Class ScheduleController
 */
@Api(tags = "排片相关")
@CrossOrigin
@RequestMapping("/schedule")
@RestController
@Validated
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation("根据排片uid获取座位信息")
    @GetMapping("/sites")
    public ScheduleHallSite[][] getHallSites(@NotBlank @RequestParam String uid){
        return scheduleService.getHallSites(uid);
    }

    @ApiOperation("根据排片id获取信息")
    @GetMapping("/info")
    public ScheduleInfoDto getScheduleInfo(@NotBlank @RequestParam String uid){
        return scheduleService.getScheduleInfo(uid);
    }

}
