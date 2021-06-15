package cn.gjyniubi.cinema.app.core.comment.dto;

import lombok.Data;

/**
 * @Author gujianyang
 * @Date 2021/6/3
 * @Class SimpleCommentDto
 */
@Data
public class SimpleCommentDto {
    private String cusName;
    private String avatar;
    private String content;
    private String createTime;
    private Integer score;
}
