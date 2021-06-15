package cn.gjyniubi.cinema.admin.core.login.controller;

import cn.gjyniubi.cinema.admin.core.login.dto.LoginUserDto;
import cn.gjyniubi.cinema.admin.core.login.service.LoginService;
import cn.gjyniubi.cinema.admin.core.login.vo.UserLoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class LoginController
 */
@RequestMapping("/user")
@CrossOrigin
@Validated
@RestController
@Api(tags = "登录相关")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public LoginUserDto login(@Validated @RequestBody UserLoginVo userLoginVo){
        return LoginUserDto.builder().token(loginService.login(userLoginVo)).build();
    }

}
