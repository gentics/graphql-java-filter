package com.gentics.mesh.graphqlfilter.filter;

import java.util.Map;

public abstract class StartMainFilter<T> extends MainFilter<T> implements StartFilter<T, Map<String, ?>> {

    public StartMainFilter(String name, String description) {
        super(name, description);
    }
}
