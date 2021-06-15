package cn.gjyniubi.cinema.app.core.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/5/25
 * @Class UpdateUserInfoVo
 */
@Data
public class UpdateUserInfoVo {
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date birthday;
    @Size(max = 30)
    @NotBlank
    private String phone;
    @Size(max = 100)
    private String avatar;
    @Size(max = 30)
    @NotBlank
    private String nickname;
    @Min(1)
    @Max(1)
    @NotNull
    private Integer sex;
}
