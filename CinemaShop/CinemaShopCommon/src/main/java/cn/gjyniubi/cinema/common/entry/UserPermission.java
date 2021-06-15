package cn.gjyniubi.cinema.common.entry;

import cn.gjyniubi.cinema.common.domain.BaseSerializable;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class UserPermission
 */
@EqualsAndHashCode(callSuper = true)
@TableName("user_permission")
@Data
public class UserPermission extends BaseSerializable {
    @TableId
    protected Integer id;
    protected String name;
    protected String path;
    protected String component;
    protected String title;
    protected String icon;
    @TableField("parent_id")
    protected Integer parentId;
    @TableField("user_type")
    protected Integer userType;
}
