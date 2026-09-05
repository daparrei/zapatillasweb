package com.example.zapatillasweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ZapatillaswebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZapatillaswebApplication.class, args);
	}

}
