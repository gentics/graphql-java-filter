package com.gentics.graphqlfilter.test.model;

import java.time.Instant;

public class Node {
    private final int id;
    private final Schema schema;
    private final Instant created;
    private final String language;

    public Node(int id, Schema schema, Instant created, String language) {
        this.id = id;
        this.schema = schema;
        this.created = created;
        this.language = language;
    }

    public Instant getCreated() {
        return created;
    }

    public String getLanguage() {
        return language;
    }

    public int getId() {
        return id;
    }

    public Schema getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return "Node{" +
            "id=" + id +
            ", schema=" + schema +
            ", created=" + created +
            ", language='" + language + '\'' +
            '}';
    }
}
