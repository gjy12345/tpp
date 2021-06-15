package cn.gjyniubi.cinema.admin;

import cn.gjyniubi.cinema.common.util.AesUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author gujianyang
 * @Date 2021/5/24
 * @Class AesTest
 */
@SpringBootTest
public class AesTest {

    @Test
    public void createAesPwd(){
        System.out.println(AesUtil.encryptAES("admin"));
    }

}
