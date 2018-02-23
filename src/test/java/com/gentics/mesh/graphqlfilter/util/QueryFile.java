package com.gentics.mesh.graphqlfilter.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Objects;

public class QueryFile {
    public final String type;
    public final String queryName;

    public QueryFile(String type, String queryName) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(queryName);
        this.type = type;
        this.queryName = queryName;
    }

    public String getQuery() {
        String path = Paths.get("/", "queries", type, queryName + ".gql").toString();
        InputStream input = QueryFile.class.getResourceAsStream(path);
        if (input == null) {
            throw new RuntimeException(String.format("Could not find query %s", path));
        }
        try {
            return IOUtils.toString(input, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
