package cn.gjyniubi.cinema.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author gujianyang
 * @Date 2021/3/7
 * @Class FileUtil
 */
public class FileUtil {

    public static boolean isImageSuffix(String suffix){
        if(suffix==null)
            return false;
        String lowerCase = suffix.toLowerCase();
        return lowerCase.equals("png")||lowerCase.equals("jpg")||
                lowerCase.equals("jpeg")||lowerCase.equals("gif");
    }

    public static boolean isImageFileName(String fileName){
        if(fileName==null||!fileName.contains("."))
            return false;
        String lowerCase = fileName.toLowerCase();
        return isImageSuffix(lowerCase.substring(lowerCase.lastIndexOf(".")+1));
    }

    //返回 .xxx
    public static String subFileNameSuffix(String fileName){
        if(fileName==null)
            throw new NullPointerException("文件名为空");
        if(!fileName.contains("."))
            throw new IllegalArgumentException("文件名中未含有 '.'");
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String saveFile(MultipartFile multipartFile, String root) throws IOException {
        File save=new File(root,UUID.randomUUID().toString()+
                subFileNameSuffix(multipartFile.getOriginalFilename()));
        multipartFile.transferTo(save);
        return save.getName();
    }

    public static boolean isSafeImageFile(String cover) {
        if(cover==null)
            return false;
        if(cover.contains("..\\")||cover.contains("../"))
            return false;
        return isImageFileName(cover);
    }
}
