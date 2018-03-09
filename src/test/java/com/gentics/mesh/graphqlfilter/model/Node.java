package com.gentics.mesh.graphqlfilter.model;

import java.time.Instant;

public class Node<T> {
    private final String uuid;
    private final Schema schema;
    private final Instant created;
    private final String language;
    private final String name;

    public Node(String uuid, Schema schema, Instant created, String language, String name) {
        this.uuid = uuid;
        this.schema = schema;
        this.created = created;
        this.language = language;
        this.name = name;
    }

    public Instant getCreated() {
        return created;
    }

    public String getLanguage() {
        return language;
    }

    public String getUuid() {
        return uuid;
    }

    public Schema getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Node{" +
            "uuid='" + uuid + '\'' +
            ", schema=" + schema +
            ", created=" + created +
            ", language='" + language + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
