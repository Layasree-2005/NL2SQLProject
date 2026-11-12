package com.tecnots.n12sql;




import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.tecnots.n12sql")
public class Nl2SqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(Nl2SqlApplication.class, args);
    }
}
