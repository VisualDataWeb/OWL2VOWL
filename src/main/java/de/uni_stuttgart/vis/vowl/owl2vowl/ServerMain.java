package de.uni_stuttgart.vis.vowl.owl2vowl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class ServerMain extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(ServerMain.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ServerMain.class);
	}
}
