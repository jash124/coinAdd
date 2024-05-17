package com.coinAdd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CoinAddApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinAddApplication.class, args);
		System.out.println("Application has been started");
	}
	
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
