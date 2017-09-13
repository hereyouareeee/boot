package com.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@Slf4j
@EnableCaching
@ServletComponentScan
public class BootApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
		log.debug("logback ....");
	}
}
