package cn.gjyniubi.cinema.app.core.film.dto;

import cn.gjyniubi.cinema.common.dto.SimpleFilmTypeDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/27
 * @Class FilmInfoDto
 */
@Data
public class FilmInfoDto {
    protected String name;
    protected String director;
    protected String uid;
    protected String duration;
    protected String region;
    protected String describe;
    protected String star;
    protected Date showTime;
    protected String cover;
    protected Integer status;
    protected String score;
    protected String lang;
    protected List<SimpleFilmTypeDto> types;
}
