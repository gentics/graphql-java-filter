package com.gentics.mesh.graphqlfilter.model;

public class Schema {
    private final String uuid;
    private final String name;

    public Schema(String uuid, String field) {
        this.uuid = uuid;
        this.name = field;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "Schema{" +
            "name='" + name + '\'' +
            '}';
    }
}
