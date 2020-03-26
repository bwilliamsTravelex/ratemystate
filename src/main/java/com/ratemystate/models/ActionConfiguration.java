package com.ratemystate.models;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.action.Actions;

@Configuration
public class ActionConfiguration {

    @Bean("Print hello")
    Action<TransferStatus, Event> printOutHello() {
        return context -> System.out.println("hello");
    }

    @Bean("Print goodbye")
    Action<TransferStatus, Event> printOutGoodbye() {
        return context -> System.out.println("goodbye");
    }
}
