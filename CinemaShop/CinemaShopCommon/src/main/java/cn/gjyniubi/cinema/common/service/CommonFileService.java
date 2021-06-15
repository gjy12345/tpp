package cn.gjyniubi.cinema.common.service;

import cn.gjyniubi.cinema.common.exception.VerifyException;
import cn.gjyniubi.cinema.common.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author gujianyang
 * @Date 2021/5/18
 * @Class CommonFileService
 */
@Service
public class CommonFileService {

    @Value("${project.file.save-path}")
    private String saveDir;

    public String uploadImage(MultipartFile file) throws IOException {
        if(!FileUtil.isImageFileName(file.getOriginalFilename())){
            throw new VerifyException("错误的文件类型!");
        }
        return FileUtil.saveFile(file,saveDir);
    }

}
