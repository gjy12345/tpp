package cn.gjyniubi.cinema.app.core.cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author gujianyang
 * @Date 2021/5/26
 * @Class SimpleCinemaDto
 */
@Data
public class SimpleCinemaDto {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected Integer id;
    protected String uid;
    protected String name;
    protected String fullPosition;
}
