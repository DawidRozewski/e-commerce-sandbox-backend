package com.dawidrozewski.sandbox.search.service;

import com.dawidrozewski.sandbox.search.SearchParameters;
import com.dawidrozewski.sandbox.search.model.Project;
import com.dawidrozewski.sandbox.search.strategy.SearchStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SearchService {

    private final Map<SearchParameters, SearchStrategy> strategyMap;

    public List<Project> search(SearchParameters searchParams) {
        SearchStrategy strategy = strategyMap.get(searchParams);
        validateStrategyPresence(searchParams, strategy);
        return strategy.search(searchParams);
    }

    private void validateStrategyPresence(SearchParameters searchParams, SearchStrategy strategy) {
        if (noStrategyFound(strategy)) {
            throw new IllegalArgumentException("No strategy found for the given search parameters: " + searchParams);
        }
    }

    private boolean noStrategyFound(SearchStrategy strategy) {
        return strategy == null;
    }
}
