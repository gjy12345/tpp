package cn.gjyniubi.cinema.app.core.film.controller;

import cn.gjyniubi.cinema.app.core.film.dto.FilmInfoDto;
import cn.gjyniubi.cinema.app.core.film.dto.FilmInfoWithSaleDataDto;
import cn.gjyniubi.cinema.app.core.film.service.FilmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @Author gujianyang
 * @Date 2021/5/27
 * @Class FilmController
 */
@Api(tags = "电影相关")
@RequestMapping("/film")
@CrossOrigin
@RestController
@Validated
public class FilmController {

    @Autowired
    private FilmService filmService;

    @ApiOperation("电影详情界面")
    @GetMapping("/info")
    public FilmInfoWithSaleDataDto getFilmInfo(@NotBlank @Size(max = 100) @RequestParam String uid){
        return filmService.getFilmInfo(uid);
    }


}
