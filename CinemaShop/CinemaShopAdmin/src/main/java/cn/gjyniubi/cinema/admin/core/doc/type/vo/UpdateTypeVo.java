package cn.gjyniubi.cinema.admin.core.doc.type.vo;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * @Author gujianyang
 * @Date 2021/5/17
 * @Class UpdateTypeVo
 */
@Data
public class UpdateTypeVo {
    @NotNull
    @Min(1)
    private Integer id;
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotNull
    @Min(0)
    @Max(1)
    private Integer status;
}
