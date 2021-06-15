package cn.gjyniubi.cinema.app.core.util;

import cn.gjyniubi.cinema.app.util.QrcodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

/**
 * @Author gujianyang
 * @Date 2021/6/2
 * @Class UtilController
 */
@Api(tags = "辅助工具接口")
@CrossOrigin
@RestController
@RequestMapping("/util")
public class UtilController {

    @ApiOperation("生成二维码")
    @GetMapping("/barcode/{content}")
    public void orderBarcode(@NotBlank @PathVariable("content") String content, HttpServletResponse response) throws Exception {
        response.setContentType("image/jpeg");
        QrcodeUtil.encode(content,response.getOutputStream());
    }

}
