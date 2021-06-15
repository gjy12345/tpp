package cn.gjyniubi.cinema.admin.core.user.service;

import cn.gjyniubi.cinema.admin.core.user.dto.UserPermissionDto;
import cn.gjyniubi.cinema.admin.core.user.mapper.PermissionMapper;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.entry.SysUser;
import cn.gjyniubi.cinema.common.entry.UserPermission;
import cn.gjyniubi.cinema.common.mapper.CommonUserMapper;
import cn.gjyniubi.cinema.common.service.CommonRedisService;
import cn.gjyniubi.cinema.common.util.BeanAssignment;
import cn.gjyniubi.cinema.common.util.HttpRequestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class CurrentUserService
 */
@Service
public class CurrentUserService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private CommonUserMapper commonUserMapper;

    @Autowired
    private CommonRedisService commonRedisService;

    public List<UserPermissionDto> getUserPermissions() {
        // 暂时为最多两层嵌套
        List<UserPermission> rootMenu = permissionMapper.selectList(new QueryWrapper<UserPermission>()
                .eq("parent_id", UserContact.ROOT_MENU)
                .eq("user_type", HttpRequestUtil.getUserType()));
        List<UserPermissionDto> permissionDtoList=new ArrayList<>();
        rootMenu.forEach((root)->{
            UserPermissionDto permissionDto = UserPermissionDto.builder()
                    .children(
                            permissionMapper.selectList(new QueryWrapper<UserPermission>()
                                    .eq("parent_id",root.getId())
                                    .eq("user_type",HttpRequestUtil.getUserType()))
                    )
                    .build();
            BeanAssignment.copySuperValues(root,permissionDto);
            if(permissionDto.getChildren().size()==0){
                permissionDto.setChildren(null);
            }
            permissionDtoList.add(permissionDto);
        });
        return permissionDtoList;
    }

    public void logout() {
        SysUser user= HttpRequestUtil.getSysUser();
        commonUserMapper.update(null,new UpdateWrapper<SysUser>().set("token",user.getToken()));
        commonRedisService.remove(UserContact.WEB_SYSTEM_TOKEN_PREFIX+user.getToken());
    }

    public SysUser getUserInfo() {
        return commonUserMapper.selectById(HttpRequestUtil.getSysUserId());
    }
}
