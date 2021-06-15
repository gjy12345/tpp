package cn.gjyniubi.cinema.app.core.user.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @Author gujianyang
 * @Date 2021/6/2
 * @Class RegisterUserVo
 */
@Data
public class RegisterUserVo {
    @Size(max = 20,min = 5)
    @NotBlank
    private String phone;
    @Size(max = 20)
    @NotBlank
    private String password;
}
