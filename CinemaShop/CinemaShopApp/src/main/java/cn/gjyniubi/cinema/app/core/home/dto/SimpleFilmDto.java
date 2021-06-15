package cn.gjyniubi.cinema.app.core.home.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author gujianyang
 * @Date 2021/5/25
 * @Class SimpleFilmDto
 */
@Data
public class SimpleFilmDto {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer id;
    private String uid;
    protected String name;
    protected String cover;
    protected String director;
    protected String duration;
    protected String region;
    @TableField("`describe`")
    protected String describe;
    protected String star;
    protected String score;
    protected String showTime;
}
