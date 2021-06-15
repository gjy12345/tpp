package cn.gjyniubi.cinema.app.core.cinema.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author gujianyang
 * @Date 2021/5/29
 * @Class CinemaDto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CinemaDto extends SimpleCinemaDto{
    protected String services;
    protected String tel;
    protected String lang;
}
