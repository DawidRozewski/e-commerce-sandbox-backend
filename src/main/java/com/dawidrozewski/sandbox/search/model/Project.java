package com.dawidrozewski.sandbox.search.model;

import liquibase.change.DatabaseChangeNote;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Project {
    private List<String> technologies;
    private String position;
    private List<String> keywords;
}
