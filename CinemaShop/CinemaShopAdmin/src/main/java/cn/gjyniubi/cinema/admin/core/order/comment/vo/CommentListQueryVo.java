package cn.gjyniubi.cinema.admin.core.order.comment.vo;

import cn.gjyniubi.cinema.common.domain.BaseTableQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/6/3
 * @Class CommentListQueryVo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentListQueryVo extends BaseTableQuery {
    @Size(max = 50)
    private String filmName;
    @Size(max = 20)
    private String phone;//用户账号
    private Date begin;
    private Date end;
}
