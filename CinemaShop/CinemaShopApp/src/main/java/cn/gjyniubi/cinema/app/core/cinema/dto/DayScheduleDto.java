package cn.gjyniubi.cinema.app.core.cinema.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/5/28
 * @Class DaySchedule
 */
@Data
public class DayScheduleDto {
    private String uid;
    @JsonFormat(pattern = "MM-dd")
    private Date day;
    private String hallName;
    private String hallUid;
    private String minPrice;
    @JsonFormat(pattern = "HH:mm")
    private Date begin;
    @JsonFormat(pattern = "HH:mm")
    private Date end;
}
