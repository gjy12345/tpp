package cn.gjyniubi.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableTransactionManagement
@SpringBootApplication
public class CinemaShopAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaShopAppApplication.class, args);
	}

}
