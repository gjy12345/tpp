package cn.gjyniubi.cinema.app.core.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/5/24
 * @Class CustomerDto
 */
@Data
public class CustomerDto {
    protected String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd")
    protected Date birthday;
    protected Integer sex;
    protected String phone;
    protected String avatar;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date lastLoginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
    protected String token;
}
