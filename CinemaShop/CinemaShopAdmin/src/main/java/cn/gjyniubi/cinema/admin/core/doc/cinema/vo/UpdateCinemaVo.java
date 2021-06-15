package cn.gjyniubi.cinema.admin.core.doc.cinema.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author gujianyang
 * @Date 2021/5/17
 * @Class UpdateCinemaVo
 */
@Data
public class UpdateCinemaVo {
    @NotNull
    @Min(1)
    private Integer id;
    @Size(max = 50,min = 1)
    @NotNull
    protected String name;
    @Size(max = 50,min = 6)
    @NotNull
    protected String position;
    @Size(max = 50,min = 1)
    @NotNull
    protected String address;
    @Size(max = 50,min = 5)
    @NotNull
    protected String tel;
    protected String services;

}
