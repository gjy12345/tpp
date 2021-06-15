package cn.gjyniubi.cinema.admin.core.doc.cinema.controller;

import cn.gjyniubi.cinema.admin.core.doc.cinema.dto.CinemaDto;
import cn.gjyniubi.cinema.admin.core.doc.cinema.service.CinemaService;
import cn.gjyniubi.cinema.admin.core.doc.cinema.vo.CreateCinemaVo;
import cn.gjyniubi.cinema.admin.core.doc.cinema.vo.QueryCinemaVo;
import cn.gjyniubi.cinema.admin.core.doc.cinema.vo.UpdateCinemaVo;
import cn.gjyniubi.cinema.common.domain.TableData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author gujianyang
 * @Date 2021/5/16
 * @Class CinemaController
 */
@CrossOrigin
@Api(tags = "电影院档案相关")
@RequestMapping("/doc/cinema")
@RestController
public class CinemaController {

    @Autowired
    private CinemaService cinemaService;


    @ApiOperation("获取电影院档案列表")
    @GetMapping("/list")
    public TableData<CinemaDto> getCinemaList(@Validated QueryCinemaVo queryCinemaVo){
        return cinemaService.getCinemaList(queryCinemaVo);
    }

    @ApiOperation("删除档案")
    @PostMapping("/delete")
    public void deleteCinema(@RequestParam Integer id){
        cinemaService.deleteCinema(id);
    }

    @ApiOperation("更新档案")
    @PostMapping("/update")
    public void updateCinema(@Validated @RequestBody UpdateCinemaVo updateCinemaVo){
        cinemaService.updateCinema(updateCinemaVo);
    }

    @ApiOperation("创建档案")
    @PostMapping("/create")
    public void createCinema(@Validated @RequestBody CreateCinemaVo createCinemaVo){
        cinemaService.createCinema(createCinemaVo);
    }

}
