package com.ratemystate;

import com.ratemystate.configuration.StateMachineAdapter;
import com.ratemystate.models.State;
import com.ratemystate.models.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statemachine/{state}")
class StateMachineController {

    @Autowired
    private StateMachineAdapter stateMachineAdapter;

    @GetMapping
    public Transfer handleAction(@PathVariable("state") String state) throws Exception {
        Transfer transfer = new Transfer(state, "Something Stupid");
        stateMachineAdapter.calculateState(transfer);
        return transfer;
    }
}
