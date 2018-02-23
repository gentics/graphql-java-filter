package com.gentics.graphqlfilter.test.model;

public class Schema {
    private final String name;

    public Schema(String field) {
        this.name = field;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Schema{" +
            "name='" + name + '\'' +
            '}';
    }
}
