package cn.gjyniubi.cinema.common.service;

import cn.gjyniubi.cinema.common.annotations.CacheFirst;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.entry.Customer;
import cn.gjyniubi.cinema.common.entry.SysUser;
import cn.gjyniubi.cinema.common.mapper.CommonUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class CommonUserService
 */
@Service
public class CommonUserService {

    @Autowired
    private CommonUserMapper commonUserMapper;

    @Autowired
    private CommonRedisService commonRedisService;

    @CacheFirst(value = "token",cache = true,prefix = UserContact.WEB_SYSTEM_TOKEN_PREFIX)
    public SysUser getUserByToken(String token, List<Integer> userTypes) {
        SysUser user=commonUserMapper.selectOne(new QueryWrapper<SysUser>().eq("token",token));
        for (Integer type : userTypes) {
            if(user.getUserType().equals(type))
                return user;
        }
        return null;
    }

    public Customer getCustomerByToken(String token,boolean ex){
        Object customer = commonRedisService.getValue(UserContact.APP_TOKEN_PREFIX + token);
        if(ex&&customer!=null){
            commonRedisService.setExpire(UserContact.APP_TOKEN_PREFIX + token,1, TimeUnit.DAYS);
        }
        return (Customer) customer;
    }

}
