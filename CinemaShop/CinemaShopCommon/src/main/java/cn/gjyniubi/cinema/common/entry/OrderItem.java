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
 * @Date 2021/5/31
 * @Class OrderItem
 * 订单子项
 */
@TableName("order_item")
@Data
public class OrderItem {
    @TableId(type = IdType.AUTO)
    protected Integer id;
    @TableField("order_id")
    protected Integer orderId;
    @TableField("`row`")
    protected Integer row;
    protected Integer col;
    protected Long price;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
    protected Integer type;
    protected Integer x;
    protected Integer y;
}
