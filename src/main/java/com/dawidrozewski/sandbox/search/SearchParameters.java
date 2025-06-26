package com.dawidrozewski.sandbox.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchParameters {
    private List<String> technologies;
    private String position;
    private List<String> keywords;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchParameters that = (SearchParameters) o;
        return (technologies == null || !technologies.isEmpty()) == (that.technologies != null && !that.technologies.isEmpty()) &&
                (position == null || !position.isEmpty()) == (that.position != null && !that.position.isEmpty()) &&
                (keywords == null || !keywords.isEmpty()) == (that.keywords != null && !that.keywords.isEmpty());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                technologies != null && !technologies.isEmpty(),
                position != null && !position.isEmpty(),
                keywords != null && !keywords.isEmpty());
    }
}
