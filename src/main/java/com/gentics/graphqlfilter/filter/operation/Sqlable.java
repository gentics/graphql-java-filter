package com.gentics.graphqlfilter.filter.operation;

import java.util.Map;

public interface Sqlable {

	String toSql();

	Map<String, String> getJoins(Map<String, String> parent);
}
