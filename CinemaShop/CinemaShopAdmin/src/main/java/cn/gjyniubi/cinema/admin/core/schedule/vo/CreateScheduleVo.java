package cn.gjyniubi.cinema.admin.core.schedule.vo;

import cn.gjyniubi.cinema.common.dto.ScheduleHallSite;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/5/23
 * @Class CreateScheduleVo
 */
@Data
public class CreateScheduleVo {
    @NotNull
    @Min(1)
    private Integer cinemaId;
    @NotNull
    @Min(1)
    private Integer hallId;
    @NotNull
    @Min(1)
    private Integer filmId;
    @NotNull
    private ScheduleHallSite[][] sites;

    @NotNull
    private Date beginTime;
    @NotNull
    private Date endTime;

    @JsonIgnore
    private String minPrice;
}
