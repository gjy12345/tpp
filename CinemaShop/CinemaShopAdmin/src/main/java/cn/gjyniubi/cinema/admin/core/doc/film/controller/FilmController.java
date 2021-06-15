package cn.gjyniubi.cinema.admin.core.doc.film.controller;

import cn.gjyniubi.cinema.admin.core.doc.film.vo.CreateFilmVo;
import cn.gjyniubi.cinema.admin.core.doc.film.dto.FilmDto;
import cn.gjyniubi.cinema.admin.core.doc.film.vo.UpdateFilmVo;
import cn.gjyniubi.cinema.admin.core.doc.film.service.FilmService;
import cn.gjyniubi.cinema.admin.core.doc.film.vo.QueryFilmVo;
import cn.gjyniubi.cinema.common.domain.TableData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author gujianyang
 * @Date 2021/5/18
 * @Class FilmController
 */
@Api(tags = "电影档案")
@RequestMapping("/doc/film")
@CrossOrigin
@RestController
public class FilmController {

    @Autowired
    private FilmService filmService;

    @ApiOperation("获取电影列表")
    @GetMapping("/list")
    public TableData<FilmDto> getList(@Validated QueryFilmVo queryFilmVo){
        return filmService.getList(queryFilmVo);
    }

    @ApiOperation("删除电影")
    @PostMapping("/delete")
    public void delete(@RequestParam Integer id){
        filmService.deleteFilm(id);
    }

    @ApiOperation("更新电影")
    @PostMapping("/update")
    public void update(@Validated @RequestBody UpdateFilmVo updateFilmVo){
        filmService.updateFilm(updateFilmVo);
    }

    @ApiOperation("创建电影")
    @PostMapping("/create")
    public void create(@Validated @RequestBody CreateFilmVo createFilmVo){
        filmService.createFilm(createFilmVo);
    }

}
