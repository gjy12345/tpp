package cn.gjyniubi.cinema.common.entry;

import cn.gjyniubi.cinema.common.domain.BaseSerializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author gujianyang
 * @Date 2021/5/17
 * @Class District
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("district")
public class District extends BaseSerializable {
    @TableId
    protected Integer id;
    protected Integer pid;
    protected String code;
    protected String name;
    protected Byte level;
}
