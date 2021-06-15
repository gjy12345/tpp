package cn.gjyniubi.cinema.app.core.comment.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author gujianyang
 * @Date 2021/6/3
 * @Class CreateCommentVo
 */
@Data
public class CreateCommentVo {
    @NotBlank
    private String orderNum;
    @NotBlank
    private String content;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer score;
}
