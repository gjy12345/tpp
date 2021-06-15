package cn.gjyniubi.cinema.admin.core.user.controller;

import cn.gjyniubi.cinema.admin.core.user.dto.UserInfoDto;
import cn.gjyniubi.cinema.admin.core.user.dto.UserPermissionDto;
import cn.gjyniubi.cinema.admin.core.user.service.CurrentUserService;
import cn.gjyniubi.cinema.common.entry.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class CurrentUserController
 * 当前用户操作
 */
@Api(tags = "当前用户操作")
@CrossOrigin
@RestController
@RequestMapping("/current/user/")
public class CurrentUserController {

    @Autowired
    private CurrentUserService currentUserService;

    @ApiOperation("获取权限菜单列表")
    @GetMapping("/permissions")
    public List<UserPermissionDto> getUserPermissions(){
        return currentUserService.getUserPermissions();
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public SysUser userInfo(){
        return currentUserService.getUserInfo();
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public void logout(){
        currentUserService.logout();
    }
}
