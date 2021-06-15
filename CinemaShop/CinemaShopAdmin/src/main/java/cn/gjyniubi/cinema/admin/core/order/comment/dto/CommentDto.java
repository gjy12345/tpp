package cn.gjyniubi.cinema.admin.core.order.comment.dto;

import cn.gjyniubi.cinema.common.entry.OrderComment;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author gujianyang
 * @Date 2021/6/3
 * @Class CommentDto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentDto extends OrderComment {
    private String orderNum;
    private String filmName;
    private String phone;//客户账号
    private String cusName;
}
