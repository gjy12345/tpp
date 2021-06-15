package cn.gjyniubi.cinema.admin.core.user.mapper;

import cn.gjyniubi.cinema.common.entry.UserPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class PermissionMapper
 */
@Mapper
@Repository
public interface PermissionMapper extends BaseMapper<UserPermission> {
}
