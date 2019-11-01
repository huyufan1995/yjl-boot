package io.renren;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan(value = { "io.renren.dao", "io.renren.cms.dao" })
@EnableAsync
public class YykjApplication {

	public static void main(String[] args) {
		SpringApplication.run(YykjApplication.class, args);
	}
}
