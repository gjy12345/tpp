package cn.gjyniubi.cinema.admin.core.doc.cinema.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @Author gujianyang
 * @Date 2021/5/17
 * @Class CreateCinemaVo
 */
@Data
public class CreateCinemaVo {
    @Size(max = 50,min = 1)
    @NotBlank
    protected String name;
    @Size(max = 50,min = 6)
    @NotBlank
    protected String position;
    @Size(max = 50,min = 1)
    @NotBlank
    protected String address;
    @Size(max = 50,min = 5)
    @NotBlank
    protected String tel;
    protected String services;
}
