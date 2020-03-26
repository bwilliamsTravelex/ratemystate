package com.ratemystate.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratemystate.models.State;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.List;

@Configuration
@Data
public class StateConfiguration {

    private ResourcePatternResolver resourceResolver;

    private List<State> states;

    @Autowired
    public StateConfiguration(ResourcePatternResolver resourceResolver) throws IOException {
        this.resourceResolver = resourceResolver;
        Resource orderResource = resourceResolver.getResource("classpath:statemachine/states.json");

        ObjectMapper jsonMapper = new ObjectMapper();
        List<State> states = jsonMapper.readValue(orderResource.getURL(), new TypeReference<List<State>>() {

        });
        System.out.println(states);
        this.states = states;
    }
}
