package edu.pkch.springbootedu;

import org.springframework.stereotype.Service;

@Service
public class Service1 {
    private Service2 service2;

    public Service1(Service2 service1) {
        this.service2 = service2;
    }
}
