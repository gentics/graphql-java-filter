package com.gentics.graphqlfilter.model;

import java.time.Instant;

public class Node {
    private final String schemaName;
    private final Instant created;
    private final String language;

    public Node(String schemaName, Instant created, String language) {
        this.schemaName = schemaName;
        this.created = created;
        this.language = language;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public Instant getCreated() {
        return created;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "Node{" +
            "schemaName='" + schemaName + '\'' +
            ", created=" + created +
            ", language='" + language + '\'' +
            '}';
    }
}
