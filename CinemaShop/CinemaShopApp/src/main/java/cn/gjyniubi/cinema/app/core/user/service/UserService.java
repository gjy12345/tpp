package cn.gjyniubi.cinema.app.core.user.service;

import cn.gjyniubi.cinema.app.core.user.dto.CustomerDto;
import cn.gjyniubi.cinema.app.core.user.mapper.UserMapper;
import cn.gjyniubi.cinema.app.core.user.vo.RegisterUserVo;
import cn.gjyniubi.cinema.app.core.user.vo.UpdateUserInfoVo;
import cn.gjyniubi.cinema.app.core.user.vo.UserLoginVo;
import cn.gjyniubi.cinema.common.annotations.TrimArgs;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.entry.Customer;
import cn.gjyniubi.cinema.common.exception.NoSuchDataException;
import cn.gjyniubi.cinema.common.exception.VerifyException;
import cn.gjyniubi.cinema.common.service.CommonRedisService;
import cn.gjyniubi.cinema.common.util.AesUtil;
import cn.gjyniubi.cinema.common.util.BeanAssignment;
import cn.gjyniubi.cinema.common.util.CustomerUtil;
import cn.gjyniubi.cinema.common.util.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author gujianyang
 * @Date 2021/5/24
 * @Class UserService
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommonRedisService commonRedisService;

    public CustomerDto login(UserLoginVo userLoginVo) {
        Customer customer = userMapper.selectOne(new QueryWrapper<Customer>()
                .eq("phone", userLoginVo.getPhone()));
        if(customer==null)
            throw new NoSuchDataException("用户不存在!");
        if(!customer.getPassword().equals(AesUtil.encryptAES(userLoginVo.getPassword())))
            throw new VerifyException("密码错误!");
        if(customer.getLocked()==UserContact.LOCKED)
            throw new VerifyException("此账号已被锁定!");
        customer.setLastLoginTime(new Date());
        userMapper.updateById(customer);
        CustomerDto customerDto = new CustomerDto();
        customerDto.setToken(UUID.randomUUID().toString());
        //保存登录凭证
        commonRedisService.setValue(UserContact.APP_TOKEN_PREFIX+customerDto.getToken(),customer,
                1, TimeUnit.DAYS);
        return BeanAssignment.copySameFields(customer, customerDto);
    }

    public CustomerDto getUserInfoByToken(String token) {
        Customer customer = commonRedisService.getValue(UserContact.APP_TOKEN_PREFIX + token);
        if (customer==null)
            throw new VerifyException("此token已过期");
        return BeanAssignment.copySameFields(customer,new CustomerDto());
    }

    public void updateUserInfo(UpdateUserInfoVo updateUserInfoVo) {
        if(updateUserInfoVo.getAvatar()!=null&&!FileUtil.isSafeImageFile(updateUserInfoVo.getAvatar())){
            throw new VerifyException("上传图片有误!");
        }
        if(updateUserInfoVo.getBirthday().after(new Date())){
            throw new VerifyException("生日时间错误!");
        }
        Customer currentCustomer = CustomerUtil.getCurrentCustomer();
        currentCustomer = userMapper.selectById(currentCustomer.getId());
        currentCustomer = BeanAssignment.copySameFields(updateUserInfoVo,currentCustomer);
        userMapper.updateById(currentCustomer);
    }

    @TrimArgs
    public void registerUser(@TrimArgs RegisterUserVo registerUserVo) {
        if (userMapper.selectCount(new QueryWrapper<Customer>()
                .eq("phone",registerUserVo.getPhone()))>0) {
            throw new VerifyException("此手机号已被注册!");
        }
        Customer customer = new Customer();
        customer.setAvatar(UserContact.DEFAULT_AVATAR);
        customer.setLocked(UserContact.UNLOCKED);
        customer.setBirthday(new Date());
        customer.setLogicDel(UserContact.LOGIC_DEL_NO_DELETE);
        customer.setCreateTime(new Date());
        customer.setPassword(AesUtil.encryptAES(registerUserVo.getPassword()));
        customer.setPhone(registerUserVo.getPhone());
        customer.setSex(UserContact.MAN);
        customer.setNickname("淘票票用户"+String.valueOf(System.currentTimeMillis()).substring(7));
        userMapper.insert(customer);
    }
}
