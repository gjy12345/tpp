package cn.gjyniubi.cinema.admin.core.doc.hall.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author gujianyang
 * @Date 2021/5/19
 * @Class UpdateHallVo
 */
@Data
public class UpdateHallVo {
    @Min(1)
    @NotNull
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String type;
    @NotNull
    @Min(0)
    @Max(1)
    private Integer status;
}
