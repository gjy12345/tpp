package cn.gjyniubi.cinema.app.core.user.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @Author gujianyang
 * @Date 2021/5/24
 * @Class CustomerLoginVo
 */
@Data
public class UserLoginVo {
    @Size(max = 20)
    @NotBlank
    private String phone;
    @Size(max = 20)
    @NotBlank
    private String password;
}
