package cn.gjyniubi.cinema.common.advice;

import cn.gjyniubi.cinema.common.domain.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author gujianyang
 * @Date 2021/2/13
 * @Class DefaultErrorAdvice
 */
@Slf4j
@RestControllerAdvice
public class DefaultErrorAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public JsonResult<Void> error(Exception e){
        log.error("全局异常捕获:",e);
        if(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes){
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes())
                    .getRequest();
            log.warn("url:{}",request.getRequestURL().toString());
        }
        if(e.getClass().getName().startsWith("cn.gjyniubi.cinema.")){
            return JsonResult.buildFail(e.getMessage());
        }
        return JsonResult.buildFail("系统错误!");
    }

    /**
     * Validator 参数校验异常处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<Object> handleMethodVoArgumentNotValidException(BindException ex) {
        FieldError err = ex.getFieldError();
        // err.getField() 读取参数字段
        // err.getDefaultMessage() 读取验证注解中的message值
        String message = "参数".concat(Objects.requireNonNull(err).getField())
                .concat(Objects.requireNonNull(err.getDefaultMessage()));
        log.info("{} -> {}", err.getObjectName(), message);
        return new ResponseEntity<>(JsonResult.buildFail(message), HttpStatus.OK);
    }

}
