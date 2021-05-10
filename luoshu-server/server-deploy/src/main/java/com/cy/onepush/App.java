package com.cy.onepush;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * application main route
 */
@SpringBootApplication(scanBasePackages = "com.cy.onepush")
@EnableAspectJAutoProxy(exposeProxy = true)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
