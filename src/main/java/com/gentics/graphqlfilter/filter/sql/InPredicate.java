package com.gentics.graphqlfilter.filter.sql;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class InPredicate extends ComparisonPredicate<String> {

	public InPredicate(List<SqlField<?>> fields, Collection<?> query, boolean escape) {
		super(" ( %s %s ( %s ) ) ", "IN", fields, query.stream().map(o -> escape ? ("'" + o.toString() + "'") : o.toString()).collect(Collectors.joining(", ")));
	}
}
