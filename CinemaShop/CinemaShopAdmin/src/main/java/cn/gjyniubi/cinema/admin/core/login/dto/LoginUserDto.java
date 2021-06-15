package cn.gjyniubi.cinema.admin.core.login.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class LoginUserDto
 */
@Builder
@Data
public class LoginUserDto {
    private String token;
}
