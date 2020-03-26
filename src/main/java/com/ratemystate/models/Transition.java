package com.ratemystate.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transition {

    public String target;

    public String guard;

    public String event;

    public String action;

}
