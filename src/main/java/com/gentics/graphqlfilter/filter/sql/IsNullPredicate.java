package com.gentics.graphqlfilter.filter.sql;

import java.util.List;

public class IsNullPredicate extends ComparisonPredicate<String> {

	public IsNullPredicate(List<String> fields) {
		super("IS", fields, "NULL", false);
	}
}
