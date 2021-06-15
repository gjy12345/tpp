package cn.gjyniubi.cinema.common.entry;

import cn.gjyniubi.cinema.common.domain.BaseSerializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class OrderCancelReason
 */
@EqualsAndHashCode(callSuper = true)
@TableName("order_cancel_reason")
@Data
public class OrderCancelReason extends BaseSerializable {
    @TableId(type = IdType.AUTO)
    protected Integer id;
    protected String reason;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date updateTime;
    @TableField("create_by")
    protected Integer createBy;
    @TableField("`status`")
    protected Integer status;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField("logic_del")
    protected Integer logicDel;


}
