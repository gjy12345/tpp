package cn.gjyniubi.cinema.common.advice;

import cn.gjyniubi.cinema.common.domain.JsonResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @Author gujianyang
 * @Date 2021/5/11
 * @Class JsonResultAdvice
 * 全局增强response
 * 统一格式处理
 */
@RestControllerAdvice(basePackages = "cn.gjyniubi.cinema")
public class JsonResultAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,@NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        if(Objects.requireNonNull(returnType.getMethod()).getReturnType()== JsonResult.class)
            return false;
        return returnType.getMethod().isAnnotationPresent(ResponseBody.class)||
                returnType.getDeclaringClass().isAnnotationPresent(RestController.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,@NonNull ServerHttpResponse response) {
        //String返回值会交给StringHttpMessageConverter 增强后会报错 也可以返回json字符串
        if(body instanceof String)//string交给不处理
            return body;
        return JsonResult.buildSuccess(body);
    }
}
