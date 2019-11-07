package edu.pkch.springbootedu;

import org.springframework.stereotype.Service;

@Service
public class Controller1 {

    private Service1 service1;
    private Service2 service2;

    public Controller1(Service1 service1, Service2 service2) {
        this.service1 = service1;
        this.service2 = service2;
    }
}

