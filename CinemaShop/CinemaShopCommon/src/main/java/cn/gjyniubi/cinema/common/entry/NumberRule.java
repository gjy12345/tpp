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
 * @Class NumberRule
 */
@Data
@TableName("number_rule")
public class NumberRule {
    @TableId(type = IdType.AUTO)
    protected Integer id;
    protected String type;
    protected Long number;
    protected Integer len;
    @TableField("`status`")
    protected Integer status;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
}
