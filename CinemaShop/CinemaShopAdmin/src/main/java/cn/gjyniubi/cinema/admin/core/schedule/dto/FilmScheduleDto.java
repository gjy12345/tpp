package cn.gjyniubi.cinema.admin.core.schedule.dto;

import cn.gjyniubi.cinema.common.entry.FilmSchedule;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author gujianyang
 * @Date 2021/5/20
 * @Class FilmScheduleDto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FilmScheduleDto extends FilmSchedule {
    private Integer sale;
    private Integer free;
    private String createUser;
    private String cinemaName;
    private String cinemaPosition;
    private String filmName;
    private String hallName;
}
