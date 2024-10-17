package com.ej4.tinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootApplication
public class Main {

    @Autowired
    private EntityManager em;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Transactional
    public void doSomething() {
        try {

            em.flush();
        } catch (Exception e) {

        }
    }
}