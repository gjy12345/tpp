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
 * @Date 2021/5/18
 * @Class DocFilm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("doc_film")
public class DocFilm extends BaseSerializable {
    @TableId(type = IdType.AUTO)
    protected Integer id;
    protected String name;
    protected String director;
    protected String uid;
    protected String duration;
    protected String region;
    @TableField("`describe`")
    protected String describe;
    protected String star;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date updateTime;
    @TableField("create_by")
    protected Integer createBy;
    @TableField("show_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date showTime;
    @TableField("logic_del")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected Integer logicDel;
    protected String cover;
    @TableField("`status`")
    protected Integer status;
    protected String score;
    protected String lang;
    protected Integer weight;

}
