package com.gentics.graphqlfilter.filter.sql;

public class NotPredicate<Q> extends AbstractPredicate<ComparisonPredicate<Q>> {

	public NotPredicate(ComparisonPredicate<Q> query) {
		super(" NOT ( %s ) ", query);
	}
}
