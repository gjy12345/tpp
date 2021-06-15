package cn.gjyniubi.cinema.common.entry;

import cn.gjyniubi.cinema.common.domain.BaseSerializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class Customer
 * 客户
 */
@TableName("customer")
@EqualsAndHashCode(callSuper = true)
@Data
public class Customer extends BaseSerializable {
    @TableId(type = IdType.AUTO)
    protected Integer id;
    protected String password;
    protected String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd")
    protected Date birthday;
    protected Integer sex;
    protected String phone;
    protected String avatar;
    protected Integer locked;
    @TableField("logic_del")
    protected Integer logicDel;
    @TableField("last_login_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date lastLoginTime;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
}
