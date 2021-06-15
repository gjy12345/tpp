package cn.gjyniubi.cinema.admin.core.login.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class UserLoginVo
 */
@Data
public class UserLoginVo {
    @Size(max = 45)
    @NotBlank
    private String username;
    @Size(max = 45)
    @NotBlank
    private String password;
}
