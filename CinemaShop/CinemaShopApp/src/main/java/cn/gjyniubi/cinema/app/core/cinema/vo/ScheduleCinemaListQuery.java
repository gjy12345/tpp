package cn.gjyniubi.cinema.app.core.cinema.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/5/27
 * @Class ScheduleCinemaListQuery
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleCinemaListQuery extends CinemaListQuery{
    @JsonIgnore
    private Integer filmId;
    @NotNull
    private Date day;
    @NotBlank
    private String filmUid;
}
