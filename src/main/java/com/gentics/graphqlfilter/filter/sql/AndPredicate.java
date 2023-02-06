package com.gentics.graphqlfilter.filter.sql;

import java.util.Arrays;
import java.util.List;

public class AndPredicate extends CombinerPredicate {

	public AndPredicate(SqlPredicate... predicates) {
		super(" AND ", Arrays.asList(predicates));
	}

	public AndPredicate(List<SqlPredicate> predicates) {
		super(" AND ", predicates);
	}
}
