package org.epo.internal.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(ServerApplication.class, args);

	}


}




