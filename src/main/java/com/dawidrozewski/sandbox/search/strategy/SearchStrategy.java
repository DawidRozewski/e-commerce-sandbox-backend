package com.dawidrozewski.sandbox.search.strategy;

import com.dawidrozewski.sandbox.search.SearchParameters;
import com.dawidrozewski.sandbox.search.model.Project;

import java.util.List;

public interface SearchStrategy {
    List<Project> search(SearchParameters parameters);
}
