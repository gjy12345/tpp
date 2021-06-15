package cn.gjyniubi.cinema.admin.core.user.dto;

import cn.gjyniubi.cinema.common.entry.UserPermission;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class UserPermissionDto
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class UserPermissionDto extends UserPermission {
    private List<UserPermission> children;
}
