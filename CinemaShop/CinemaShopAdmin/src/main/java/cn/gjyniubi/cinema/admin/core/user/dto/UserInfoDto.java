package cn.gjyniubi.cinema.admin.core.user.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class UserInfoDto
 */
@Builder
@Data
public class UserInfoDto {
    private String[] roles;
    private String introduction;
    private String avatar;
    private String name;
}
