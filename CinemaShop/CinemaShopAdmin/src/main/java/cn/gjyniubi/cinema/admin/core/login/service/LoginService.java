package cn.gjyniubi.cinema.admin.core.login.service;

import cn.gjyniubi.cinema.admin.core.login.vo.UserLoginVo;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.entry.SysUser;
import cn.gjyniubi.cinema.common.exception.VerifyException;
import cn.gjyniubi.cinema.common.mapper.CommonUserMapper;
import cn.gjyniubi.cinema.common.service.CommonRedisService;
import cn.gjyniubi.cinema.common.util.AesUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class LoginService
 */
@Service
public class LoginService {

    @Autowired
    private CommonUserMapper commonUserMapper;

    @Autowired
    private CommonRedisService commonRedisService;

    public String login(UserLoginVo userLoginVo){
        SysUser sysUser = commonUserMapper.selectOne(new QueryWrapper<SysUser>()
                .eq("username", userLoginVo.getUsername()));
        if(sysUser==null)
            throw new VerifyException("账号不存在!");
        if(!sysUser.getPassword().equals(AesUtil.encryptAES(userLoginVo.getPassword())))
            throw new VerifyException("密码错误!");
        if(sysUser.getLocked()== UserContact.LOCKED)
            throw new VerifyException("此账号已被锁定!");
        if(StringUtils.isNotBlank(sysUser.getToken())){
            commonRedisService.remove(UserContact.WEB_SYSTEM_TOKEN_PREFIX+sysUser.getToken());
        }
        String token= UUID.randomUUID().toString();
        //WEB_SYSTEM_TOKEN_PREFIX
        commonUserMapper.update(null,new UpdateWrapper<SysUser>().set("token",token));
        commonRedisService.setValue(UserContact.WEB_SYSTEM_TOKEN_PREFIX+token,sysUser,4, TimeUnit.HOURS);
        return token;
    }

}
