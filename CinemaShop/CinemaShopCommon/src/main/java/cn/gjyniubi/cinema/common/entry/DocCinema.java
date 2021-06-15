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
 * @Date 2021/5/16
 * @Class DocCinema
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("doc_cinema")
public class DocCinema extends BaseSerializable {
    @TableId(type = IdType.AUTO)
    protected Integer id;
    protected String uid;
    protected String name;
    protected String position;
    protected String address;
    protected String tel;
    protected String services;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date updateTime;
    @TableField("create_by")
    protected Integer createBy;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField("logic_del")
    protected Integer logicDel;
    @TableField("`status`")
    protected Integer status;


}
