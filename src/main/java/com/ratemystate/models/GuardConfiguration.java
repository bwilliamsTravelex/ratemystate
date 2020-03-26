package com.ratemystate.models;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.guard.Guard;

@Configuration
public class GuardConfiguration {

    /**
     *  Compound Transitions with && and ||
     */
    @Bean("Is transfer required details provided")
    Guard<TransferStatus, Event> isTransferRequiredDetailsProvided() {
        return context -> true;
    }

    @Bean("Is transfer reviewed details provided")
    Guard<TransferStatus, Event> isTransferReviewedDetailsProvided() {
        return context -> false;
    }

}
