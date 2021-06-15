package cn.gjyniubi.cinema.admin.core.user.vo;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * @Author gujianyang
 * @Date 2021/5/24
 * @Class UpdateCustomerVo
 */
@Data
public class UpdateCustomerVo {
    @Min(1)
    @NotNull
    private Integer id;
    @NotBlank
    @Size(max = 40)
    private String nickname;
    @Size(max = 50)
    private String avatar;
    @Min(0)
    @Max(1)
    @NotNull
    private Integer sex;
    @NotNull
    @Max(1)
    @Min(0)
    private Integer locked;
    @Size(max = 20)
    @NotBlank
    private String phone;
}
