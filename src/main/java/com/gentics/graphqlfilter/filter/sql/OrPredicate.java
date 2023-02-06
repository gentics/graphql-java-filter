package com.gentics.graphqlfilter.filter.sql;

import java.util.Arrays;
import java.util.List;

public class OrPredicate extends CombinerPredicate {

	public OrPredicate(SqlPredicate... predicates) {
		super(" OR ", Arrays.asList(predicates));
	}

	public OrPredicate(List<SqlPredicate> predicates) {
		super(" OR ", predicates);
	}
}
