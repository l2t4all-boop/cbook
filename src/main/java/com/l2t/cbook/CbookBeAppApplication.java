package com.l2t.cbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration.class)
@EnableAsync
@EnableScheduling
public class CbookBeAppApplication  {

	public static void main(String[] args) {
		SpringApplication.run(CbookBeAppApplication.class, args);
	}
}
