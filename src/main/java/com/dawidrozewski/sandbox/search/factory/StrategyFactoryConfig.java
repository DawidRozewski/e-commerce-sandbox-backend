package com.dawidrozewski.sandbox.search.factory;

import com.dawidrozewski.sandbox.search.SearchParameters;
import com.dawidrozewski.sandbox.search.strategy.SearchStrategy;
import com.dawidrozewski.sandbox.search.strategy.impl.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class StrategyFactoryConfig {
    private final String SOFTWARE_ENGINEER = "Software Engineer";
    private final List<String> TECHNOLOGIES = List.of("Java", "Spring Boot");
    private final List<String> KEYWORDS = List.of("remote", "backend");

    private final AllParametersStrategy allParametersStrategy;
    private final KeywordOnlyStrategy keywordOnlyStrategy;
    private final PositionOnlyStrategy positionOnlyStrategy;
    private final PositionAndKeywordStrategy positionAndKeywordStrategy;
    private final TechnologyOnlyStrategy technologyOnlyStrategy;
    private final TechnologyAndKeywordStrategy technologyAndKeywordStrategy;
    private final TechnologyAndPositionStrategy technologyAndPositionStrategy;


    @Bean
    public Map<SearchParameters, SearchStrategy> strategyMap() {
        Map<SearchParameters, SearchStrategy> strategies = new HashMap<>();

        strategies.put(new SearchParameters(null, null, KEYWORDS), keywordOnlyStrategy);
        strategies.put(new SearchParameters(null, SOFTWARE_ENGINEER, null), positionOnlyStrategy);
        strategies.put(new SearchParameters(null, SOFTWARE_ENGINEER, KEYWORDS), positionAndKeywordStrategy);
        strategies.put(new SearchParameters(TECHNOLOGIES, null, null), technologyOnlyStrategy);
        strategies.put(new SearchParameters(TECHNOLOGIES, null, KEYWORDS), technologyAndKeywordStrategy);
        strategies.put(new SearchParameters(TECHNOLOGIES, SOFTWARE_ENGINEER, null), technologyAndPositionStrategy);
        strategies.put(new SearchParameters(TECHNOLOGIES, SOFTWARE_ENGINEER, KEYWORDS), allParametersStrategy);

        return strategies;
    }

}
