package cn.gjyniubi.cinema.admin.core.dashboard.controller;

import cn.gjyniubi.cinema.admin.core.dashboard.domain.DataItem;
import cn.gjyniubi.cinema.admin.core.dashboard.service.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/6/2
 * @Class DashboardController
 */
@Api(tags = "首页")
@CrossOrigin
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @ApiOperation("获取首页信息")
    @GetMapping("/data")
    public List<DataItem> getDashboardData(){
        return dashboardService.getDashboardData();
    }
}
