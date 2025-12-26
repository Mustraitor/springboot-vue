package edu.friday;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//implements CommandLineRunner
@SpringBootApplication
public class FridayApplication {

//    @Autowired
//    private AdminPasswordService passwordService;

    public static void main(String[] args) {
        SpringApplication.run(FridayApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        passwordService.resetAdminPassword();
//    }
}
