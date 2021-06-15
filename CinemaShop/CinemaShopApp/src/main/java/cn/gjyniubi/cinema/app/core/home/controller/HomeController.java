package cn.gjyniubi.cinema.app.core.home.controller;

import cn.gjyniubi.cinema.app.core.home.dto.SimpleFilmDto;
import cn.gjyniubi.cinema.app.core.home.service.HomeService;
import cn.gjyniubi.cinema.common.domain.BaseListQuery;
import cn.gjyniubi.cinema.common.domain.ListData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author gujianyang
 * @Date 2021/5/25
 * @Class HomeController
 */
@Api(tags = "首页接口相关")
@CrossOrigin
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @ApiOperation("获取正在热映")
    @GetMapping("/hots")
    public ListData<SimpleFilmDto> getNowShowingFilm(BaseListQuery baseListQuery){
        return homeService.getNowShowingFilm(baseListQuery);
    }

    @ApiOperation("获取即将上映")
    @GetMapping("/willShow")
    public ListData<SimpleFilmDto> getWillShowFilm(BaseListQuery baseListQuery){
        return homeService.getWillShowFilm(baseListQuery);
    }

}
