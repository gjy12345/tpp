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
 * @Class Order
 * 订单
 */
@TableName("`order`")
@Data
public class Order {
    @TableId(type = IdType.AUTO)
    protected Integer id;
	@TableField("order_num")
	protected String orderNum;
	@TableField("`status`")
	protected Integer status;
	protected Long price;
	@TableField("cus_id")
	protected Integer cusId;
	@TableField("create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date createTime;
	@TableField("schedule_id")
	protected Integer scheduleId;
	@TableField("common_status")
	protected Integer commonStatus;
	protected String phone;
	@TableField("pay_time")
	protected Date payTime;

}
