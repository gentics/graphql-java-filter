package com.gentics.graphqlfilter.filter.sql;

import java.util.Collection;
import java.util.stream.Collectors;

public class InPredicate extends ComparisonPredicate<String> {

	public InPredicate(String field, Collection<?> query, boolean escape) {
		super(" ( %s %s ( %s ) ) ", "IN", field, query.stream().map(o -> escape ? ("'" + o.toString() + "'") : o.toString()).collect(Collectors.joining(", ")));
	}
}
