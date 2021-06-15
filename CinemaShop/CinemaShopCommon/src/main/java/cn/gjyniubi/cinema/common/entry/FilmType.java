package cn.gjyniubi.cinema.common.entry;

import cn.gjyniubi.cinema.common.domain.BaseSerializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author gujianyang
 * @Date 2021/5/18
 * @Class FilmType
 */
@EqualsAndHashCode(callSuper = true)
@TableName("film_types")
@Data
public class FilmType extends BaseSerializable {
    @TableId(type = IdType.AUTO)
    protected Integer id;
    @TableField("film_id")
    protected Integer filmId;
    @TableField("film_type_id")
    protected Integer filmTypeId;
}
