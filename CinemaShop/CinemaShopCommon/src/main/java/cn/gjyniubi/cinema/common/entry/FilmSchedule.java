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
 * @Date 2021/5/20
 * @Class FilmSchedule
 */
@Data
@TableName("film_schedule")
public class FilmSchedule {
    @TableId(type = IdType.AUTO)
    protected Integer id;
    @TableField("cinema_id")
    protected Integer cinemaId;
    @TableField("film_id")
    protected Integer filmId;
    @TableField("hall_id")
    protected Integer hallId;
    @TableField("site_info")
    protected String siteInfo;
    @TableField("begin_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date beginTime;
    @TableField("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date endTime;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
    @TableField("create_by")
    protected Integer createBy;
    @TableField("logic_del")
    protected Integer logicDel;
    @TableField("total_site")
    protected Integer totalSite;
    @TableField("min_price")
    protected String minPrice;
    protected String uid;
    protected Integer version;
}
