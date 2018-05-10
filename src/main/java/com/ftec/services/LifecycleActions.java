package com.ftec.services;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class LifecycleActions {
    @PostConstruct
    public void init(){

    }

    @PreDestroy
    public void destroy(){

    }
}
