package cn.gjyniubi.cinema.admin.core.order.reason.vo;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * @Author gujianyang
 * @Date 2021/5/13
 * @Class CreateReasonVo
 */
@Data
public class CreateReasonVo {
    @NotBlank
    @Size(max = 100)
    private String reason;
    @Max(1)
    @Min(0)
    @NotNull
    private Integer status;
}
