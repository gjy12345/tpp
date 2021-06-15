package cn.gjyniubi.cinema.common.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/6/3
 * @Class FilmComment
 */
@Data
@TableName("order_comment")
public class OrderComment {
    @TableId(type = IdType.AUTO)
    protected Integer id;
    @TableField("film_id")
    protected Integer filmId;
    @TableField("schedule_id")
    protected Integer scheduleId;
    protected Integer score;
    @TableField("cus_id")
    protected Integer cusId;
    @TableField("order_id")
    protected Integer orderId;
    protected String content;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
    @TableField("logic_del")
    protected Integer logicDel;
}
