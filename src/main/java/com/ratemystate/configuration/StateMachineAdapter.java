package com.ratemystate.configuration;

import com.ratemystate.models.Event;
import com.ratemystate.models.State;
import com.ratemystate.models.Transfer;
import com.ratemystate.models.TransferStatus;
import com.ratemystate.statemachine.DynamicConfig;
import com.ratemystate.statemachine.StateMachineLoggingInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StateMachineAdapter {

    @Autowired
    public DynamicConfig dynamicConfig;

    @Autowired
    private StateMachinePersister<TransferStatus, Event, Transfer> persister;


    public State calculateState(Transfer transfer) throws Exception {
        log.info("Calculating state from transfer {}", transfer.getStatus());
        StateMachine<TransferStatus, Event> machine = dynamicConfig.buildMachine();
        machine.getStateMachineAccessor().doWithAllRegions(sma -> {
            StateMachineContext<TransferStatus, Event> regionContext =
                            new DefaultStateMachineContext<TransferStatus, Event>(
                                            TransferStatus.valueOf(transfer.getStatus()), null, null, new DefaultExtendedState());
            sma.resetStateMachine(regionContext);
            sma.addStateMachineInterceptor(new StateMachineLoggingInterceptor());
        });
        machine.start();
        machine.sendEvent(Event.UPDATED);
        persister.persist(machine, transfer);
        log.info("State ended up as {}", transfer.getStatus());
        return null;
    }

}
