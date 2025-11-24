package com.example.pedidosYA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class PedidosYaApplication {
	public static void main(String[] args) {
		SpringApplication.run(PedidosYaApplication.class, args);
	}

}
