package cn.gjyniubi.cinema.app.core.user.controller;

import cn.gjyniubi.cinema.app.core.user.dto.CustomerDto;
import cn.gjyniubi.cinema.app.core.user.service.UserService;
import cn.gjyniubi.cinema.app.core.user.vo.RegisterUserVo;
import cn.gjyniubi.cinema.app.core.user.vo.UpdateUserInfoVo;
import cn.gjyniubi.cinema.app.core.user.vo.UserLoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author gujianyang
 * @Date 2021/5/24
 * @Class UserController
 */
@RequestMapping("/user")
@Api(tags = "用户相关")
@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public CustomerDto login(@Validated @RequestBody UserLoginVo userLoginVo){
        return userService.login(userLoginVo);
    }

    @ApiOperation("根据上一次登录缓存的token获取用户信息")
    @GetMapping("/checkToken")
    public CustomerDto getUserInfoByToken(@RequestParam String token){
        return userService.getUserInfoByToken(token);
    }

    @ApiOperation("更新用户信息")
    @PostMapping("/updateInfo")
    public void updateUserInfo(@Validated @RequestBody UpdateUserInfoVo updateUserInfoVo){
        userService.updateUserInfo(updateUserInfoVo);
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public void register(@Validated @RequestBody RegisterUserVo registerUserVo){
        userService.registerUser(registerUserVo);
    }

}
