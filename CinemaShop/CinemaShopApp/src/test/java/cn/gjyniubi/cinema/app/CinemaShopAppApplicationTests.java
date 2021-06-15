package cn.gjyniubi.cinema.app;

import cn.gjyniubi.cinema.common.mapper.CommonFilmDocMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CinemaShopAppApplicationTests {

	@Autowired
	private CommonFilmDocMapper filmDocMapper;

	@Test
	void contextLoads() {
		filmDocMapper.updateScore(3);
	}

}
