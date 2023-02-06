package com.gentics.graphqlfilter.filter.sql;

public class IsNullPredicate extends ComparisonPredicate<String> {

	public IsNullPredicate(String field, boolean escape) {
		super("IS", field, "NULL", escape);
	}
}
