package cn.gjyniubi.cinema.common.controller;

import cn.gjyniubi.cinema.common.domain.JsonResult;
import cn.gjyniubi.cinema.common.service.CommonFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author gujianyang
 * @Date 2021/5/18
 * @Class CommonFileController
 */
@Api(tags = "通用文件上传")
@CrossOrigin
@RequestMapping("/common/file")
@RestController
public class CommonFileController {

    @Autowired
    private CommonFileService commonFileService;

    @ApiOperation("上传图片")
    @PostMapping("/uploadImages")
    public JsonResult<String> uploadFile(@RequestParam MultipartFile file) throws IOException {
        return JsonResult.buildSuccess(commonFileService.uploadImage(file));
    }

}
