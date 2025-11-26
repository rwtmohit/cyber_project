package com.example.cyber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CyberApplication {

	public static void main(String[] args) {
		 System.out.println("------------------------------------------------------");
        System.out.println("   🚀 Cyber Security Scanner Backend Started");
        System.out.println("   📡 API Base URL: http://localhost:8080/api/v1/security");
        System.out.println("------------------------------------------------------");
		SpringApplication.run(CyberApplication.class, args);
	}




}
