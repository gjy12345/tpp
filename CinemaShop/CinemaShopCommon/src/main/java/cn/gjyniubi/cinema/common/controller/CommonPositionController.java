package cn.gjyniubi.cinema.common.controller;

import cn.gjyniubi.cinema.common.entry.District;
import cn.gjyniubi.cinema.common.service.CommonPositionService;
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
 * @Date 2021/5/16
 * @Class CommonPositionController
 */
@RequestMapping("/common/position")
@CrossOrigin
@Api(tags = "地图相关")
@RestController
public class CommonPositionController {

    @Autowired
    private CommonPositionService commonPositionService;

    @ApiOperation("获取省")
    @GetMapping("/provinces")
    public List<District> getProvinces(){
        return commonPositionService.getProvinces();
    }

    @ApiOperation("获取城市")
    @GetMapping("/cities")
    public List<District> getCities(Integer provinceId){
        return commonPositionService.getCities(provinceId);
    }

    @ApiOperation("获取县")
    @GetMapping("/countries")
    public List<District> getCountries(Integer cityId){
        return commonPositionService.getCountries(cityId);
    }

}
