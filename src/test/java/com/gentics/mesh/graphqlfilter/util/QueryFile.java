package com.gentics.mesh.graphqlfilter.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class QueryFile {
    public final String[] path;

    public QueryFile(String... path) {
        Objects.requireNonNull(path);
        this.path = path;
    }

    public String getQuery() {
        path[path.length - 1] = path[path.length - 1] + ".gql";
        String path = Paths.get("/", flatten(
            new String[]{"queries"},
            this.path
        )).toString();
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

    private static String[] flatten(String[]... values) {
        return Arrays.stream(values)
            .flatMap(Arrays::stream)
            .toArray(String[]::new);
    }
}
