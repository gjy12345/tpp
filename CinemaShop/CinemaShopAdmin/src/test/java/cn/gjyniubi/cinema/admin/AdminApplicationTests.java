package cn.gjyniubi.cinema.admin;

import cn.gjyniubi.cinema.admin.core.doc.hall.service.HallService;
import cn.gjyniubi.cinema.admin.core.doc.hall.vo.CreateHallVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdminApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private HallService hallService;

	@Test
	public void testTrim(){
		CreateHallVo createHallVo=new CreateHallVo();
		createHallVo.setName("  999999   ");
		createHallVo.setType(null);
		hallService.createHall(createHallVo);
	}
}
