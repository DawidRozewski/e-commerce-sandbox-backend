package com.dawidrozewski.sandbox.search.strategy.impl;

import com.dawidrozewski.sandbox.search.SearchParameters;
import com.dawidrozewski.sandbox.search.model.Project;
import com.dawidrozewski.sandbox.search.strategy.SearchStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AllParametersStrategy implements SearchStrategy {

    @Override
    public List<Project> search(SearchParameters parameters) {
        log.info("STRATEGY ----> " + AllParametersStrategy.class.getName() + " [1]");
        return List.of();
    }
}

