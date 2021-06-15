package cn.gjyniubi.cinema.common.advice;

import cn.gjyniubi.cinema.common.domain.JsonResult;
import cn.gjyniubi.cinema.common.enums.JsonResultType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author gujianyang
 * @Date 2021/2/13
 * @Class NotFoundErrorAdvice
 */
@RestController
@Slf4j
public class NotFoundErrorAdvice implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public JsonResult<Void> error(){
        log.warn("404!");
        return JsonResult.buildFail(JsonResultType.NOT_FOUND,"您的资源未找到!");
    }

}
