package cn.gjyniubi.cinema.admin;

import cn.gjyniubi.cinema.common.util.GenerateCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class GenerateClassTest
 */
@SpringBootTest
public class GenerateClassTest {
    @Autowired
    private GenerateCode generateCode;

    @Test
    public void generateClass() throws SQLException {
        generateCode.generateCode("`film_comment`");
//        generateCode.generateCode("order_item");
    }
}
