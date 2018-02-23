package com.gentics.mesh.graphqlfilter.model;

import java.time.Instant;

public class Node {
    private final String uuid;
    private final Schema schema;
    private final Instant created;
    private final String language;

    public Node(String uuid, Schema schema, Instant created, String language) {
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
    }

    public Schema getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return "Node{" +
            "uuid=" + uuid +
            ", schema=" + schema +
            ", created=" + created +
            ", language='" + language + '\'' +
            '}';
    }
}
