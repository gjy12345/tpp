package cn.gjyniubi.cinema.app.core.schedule.dto;

import cn.gjyniubi.cinema.app.core.cinema.dto.CinemaDto;
import cn.gjyniubi.cinema.app.core.film.dto.FilmInfoDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/5/31
 * @Class ScheduleDto
 */
@Builder
@Data
public class ScheduleInfoDto {

    private String hallName;
    private FilmInfoDto filmInfoDto;
    private CinemaDto cinemaDto;
    @JsonFormat(pattern = "MM-dd HH:mm:ss")
    private Date begin;
    @JsonFormat(pattern = "MM-dd HH:mm:ss")
    private Date end;


}
