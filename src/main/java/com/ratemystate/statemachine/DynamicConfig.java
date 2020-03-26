package com.ratemystate.statemachine;

import com.ratemystate.configuration.StateConfiguration;
import com.ratemystate.models.Event;
import com.ratemystate.models.TransferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.guard.Guard;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
@EnableStateMachineFactory
public class DynamicConfig extends EnumStateMachineConfigurerAdapter<TransferStatus, Event> {

    @Autowired
    private StateConfiguration stateConfiguration;

    @Autowired
    private ApplicationContext context;

    private StateMachineBuilder.Builder<TransferStatus, Event> builder;

    public StateMachine<TransferStatus, Event> buildMachine() throws Exception {
        builder = StateMachineBuilder.builder();
        builder.configureConfiguration().withConfiguration().autoStartup(true).beanFactory(context.getAutowireCapableBeanFactory());
        builder.configureStates()
               .withStates()
               .initial(TransferStatus.DRAFT)
               .end(TransferStatus.SUBMITTED)
               .states(new HashSet(Arrays.asList(TransferStatus.DRAFT,
                                                 TransferStatus.UNDER_REVIEW,
                                                 TransferStatus.REQUIREMENTS_READY,
                                                 TransferStatus.REVIEWED,
                                                 TransferStatus.SUBMITTED)));
        builder.configureTransitions().withExternal().event(Event.UPDATED);
        stateConfiguration.getStates().forEach(state -> {
            state.getTransitions().forEach(transition -> {
                configureTransitions(TransferStatus.valueOf(state.getSourceState()),
                                     TransferStatus.valueOf(transition.getTarget()),
                                     transition.getGuard(),
                                     transition.getAction());
            });
        });
        return builder.build();
    }

    public void configureTransitions(TransferStatus sourceState, TransferStatus targetState, String guardQualifierName,
                                     String actionQualifierName) {
        try {
            builder.configureTransitions()
                   .withExternal()
                   .source(sourceState)
                   .target(targetState)
                   .guard(findGuardViaQualifier(guardQualifierName))
                   .action(findActionViaQualifier(actionQualifierName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Action<TransferStatus, Event> findActionViaQualifier(String actionQualifierName) {
        return (Action<TransferStatus, Event>) context.getBean(actionQualifierName);
    }

    private Guard<TransferStatus, Event> findGuardViaQualifier(String guardQualifierName) {
        return (Guard<TransferStatus, Event>) context.getBean(guardQualifierName);
    }

}
