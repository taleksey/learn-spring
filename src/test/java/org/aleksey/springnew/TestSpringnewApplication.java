package org.aleksey.springnew;

import org.springframework.boot.SpringApplication;

public class TestSpringnewApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringnewApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
