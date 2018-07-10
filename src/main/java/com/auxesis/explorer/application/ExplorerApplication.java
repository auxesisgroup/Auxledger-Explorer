package com.auxesis.explorer.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;




//@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@EntityScan("com.auxesis.explorer.entities")
@EnableJpaRepositories(basePackages = "com.auxesis.explorer.repository")
@SpringBootApplication(scanBasePackages = { "com.auxesis.explorer" })
public class ExplorerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExplorerApplication.class, args);
	}
}
