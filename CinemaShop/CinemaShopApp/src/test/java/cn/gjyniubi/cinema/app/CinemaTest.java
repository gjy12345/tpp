package cn.gjyniubi.cinema.app;

import cn.gjyniubi.cinema.app.core.cinema.service.CinemaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author gujianyang
 * @Date 2021/5/28
 * @Class CinemaTest
 */
@SpringBootTest
public class CinemaTest {

    @Autowired
    private CinemaService cinemaService;

    @Test
    public void testGetScheduleFilms(){
        System.out.println(cinemaService.getScheduleFilms(""));
    }
}
