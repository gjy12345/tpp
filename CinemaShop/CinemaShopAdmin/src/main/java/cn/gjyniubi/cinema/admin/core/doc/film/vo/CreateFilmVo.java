package cn.gjyniubi.cinema.admin.core.doc.film.vo;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/18
 * @Class CreateFilmVo
 */
@Data
public class CreateFilmVo {
    private List<Integer> type;
    @Size(max = 200)
    @NotBlank
    protected String name;
    @Size(max = 200)
    @NotBlank
    protected String director;
    @Size(max = 48)
    @NotBlank
    protected String duration;
    @Size(max = 48)
    @NotBlank
    protected String region;
    @Size(max = 500)
    @NotBlank
    protected String describe;
    @Size(max = 1000)
    @NotBlank
    protected String star;
    @NotNull
    protected Date showTime;
    @Size(max = 150)
    protected String cover;
    @Min(0)
    @Max(1)
    @NotNull
    protected Integer status;
    protected String score;
    protected String lang;
    @NotNull
    @Min(0)
    protected Integer weight;
}
