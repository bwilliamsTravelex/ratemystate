// Copyright (c) 2019 Travelex Ltd

package com.ratemystate.statemachine;

import com.ratemystate.models.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;

@AllArgsConstructor
@Slf4j
public class StateMachineLoggingInterceptor extends StateMachineInterceptorAdapter {


    @Override
    public void preStateChange(State state, Message message, Transition transition, StateMachine stateMachine, StateMachine rootStateMachine) {
        try {
            if (transition.getSource() != null && transition.getTarget() != null) {
                if (message != null) {
                    log.info("[STATE CHANGE] [{} -----> {}] by event [{}]",
                             getInternalStatus(transition.getSource().getId()),
                             getInternalStatus(transition.getTarget().getId()),
                             message.getPayload());
                } else {
                    log.info("[STATE CHANGE][{} -----> {}]",
                             getInternalStatus(transition.getSource().getId()),
                             getInternalStatus(transition.getTarget().getId()));
                }
            }
        } catch (Exception e) {
            log.error("Error during interceptor", e);
        }
    }

    private String getInternalStatus(Object status) {
        return ((TransferStatus) status).name();
    }

}
