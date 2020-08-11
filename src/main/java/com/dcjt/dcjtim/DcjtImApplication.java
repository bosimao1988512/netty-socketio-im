package com.dcjt.dcjtim;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@MapperScan("com.dcjt.dcjtim.repository")
public class DcjtImApplication {

	public static void main(String[] args) {
		SpringApplication.run(DcjtImApplication.class, args);

		//new SpringApplicationBuilder(DcjtImApplication.class).bannerMode(Banner.Mode.OFF).run(args);
	}
}
