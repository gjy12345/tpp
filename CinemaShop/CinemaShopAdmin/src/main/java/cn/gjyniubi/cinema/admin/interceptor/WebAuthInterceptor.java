package cn.gjyniubi.cinema.admin.interceptor;


import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.domain.JsonResult;
import cn.gjyniubi.cinema.common.enums.JsonResultType;
import cn.gjyniubi.cinema.common.enums.SystemUserType;
import cn.gjyniubi.cinema.common.service.CommonUserService;
import cn.gjyniubi.cinema.common.util.HttpRequestUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/3/2
 * @Class AppAuthInterceptor
 * 检验app请求是否已经登录
 */
@Slf4j
@Component
public class WebAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private CommonUserService userService;

    @Autowired
    private Gson gson;

    //只允许管理员或合作商用户
    private static final List<Integer> allowUserType= Arrays.asList(SystemUserType.ADMIN.getValue(),
            SystemUserType.PARTNER.getValue());

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        String token=request.getHeader(UserContact.TOKEN_HEADER);
        if(request.getMethod().equalsIgnoreCase("OPTIONS"))
            return true;
        boolean result;
        if(token==null)
            result=false;
        else{
            HttpRequestUtil.setSysUser(userService.getUserByToken(token,allowUserType));
            //设置用户信息
            result=HttpRequestUtil.getSysUser()!=null;
        }
        if(!result){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(gson.toJson(JsonResult.buildFail(JsonResultType.NO_LOGIN,"请先登录后操作!")));
        }
        log.info("token:{},{}",token+"",request.getRequestURL().toString());
        log.info("token鉴权结果:{},用户身份:{}",result,HttpRequestUtil.getUserType()+"");
        return result&&HttpRequestUtil.isAllowUser(allowUserType);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler, Exception ex) throws Exception {
        HttpRequestUtil.removeSysUser();//清除用户信息
    }
}
