package cn.gjyniubi.cinema.app.core.cinema.dto;

import cn.gjyniubi.cinema.app.core.film.dto.FilmInfoDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/28
 * @Class FilmSchedule
 */
@Data
public class FilmScheduleDto {
    @JsonProperty(value = "film")
    private FilmInfoDto filmInfoDto;
    @JsonProperty("days")
    private List<DaySchedule> daySchedules;

    @Data
    public static class DaySchedule{
        @JsonFormat(pattern = "MM-dd")
        private Date day;
        private List<DayScheduleDto> scheduleList;
    }
}
