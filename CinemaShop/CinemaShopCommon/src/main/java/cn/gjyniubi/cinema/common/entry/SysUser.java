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
 * @Class SysUser
 * 后台管理用户:管理员，合作商
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user")
public class SysUser extends BaseSerializable {
    @TableId(type = IdType.AUTO)
    protected Integer id;
    protected String username;
    protected String name;
    protected String avatar;
    @TableField("user_type")
    protected Integer userType;
    protected String password;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date updateTime;
    protected Integer locked;
    @TableField("logic_del")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected Integer logicDel;
    protected String token;


}
