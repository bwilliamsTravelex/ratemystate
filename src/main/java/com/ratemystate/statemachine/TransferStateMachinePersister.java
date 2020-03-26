// Copyright (c) 2018 Travelex Ltd

package com.ratemystate.statemachine;

import com.ratemystate.models.Event;
import com.ratemystate.models.Transfer;
import com.ratemystate.models.TransferStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransferStateMachinePersister extends DefaultStateMachinePersister<TransferStatus, Event, Transfer> {

    public TransferStateMachinePersister() {
        super(new TransferStateMachinePersist());
    }

    public static class TransferStateMachinePersist implements StateMachinePersist<TransferStatus, Event, Transfer> {

        @Override
        public StateMachineContext<TransferStatus, Event> read(Transfer transfer) {
            log.error("An attempted call was made to read transfer {}, however we no longer persist context objects of SM", "transfer");
            throw new UnsupportedOperationException("Context persisting is not supported.");
        }

        @Override
        public void write(StateMachineContext<TransferStatus, Event> context, Transfer transfer) {
            setTransferState(context, transfer);
            log.debug("<~~~~~~~~ State Machine Action [PERSISTING] {} as {}", "tranfser", transfer.getStatus());
        }

        private void setTransferState(StateMachineContext<TransferStatus, Event> context, Transfer transfer) {
            TransferStatus transferStatus = context.getState();
            if (!context.getChilds().isEmpty()) {
                transferStatus = findDeepState(context);
            }
            transfer.setStatus(transferStatus.name());
        }

        private TransferStatus findDeepState(StateMachineContext<TransferStatus, Event> context) {
            if (context.getChilds().isEmpty()) {
                log.debug("Deepest state found as {}", context.getState().name());
                return context.getState();
            }
            log.debug("Diving into state {}", context.getChilds().get(0).getState().name());
            return findDeepState(context.getChilds().get(0));
        }

    }
}
