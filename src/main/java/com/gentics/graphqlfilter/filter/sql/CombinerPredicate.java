package com.gentics.graphqlfilter.filter.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CombinerPredicate extends AbstractPredicate<List<SqlPredicate>> {

	public CombinerPredicate(String operator) {
		this(operator, new ArrayList<>());
	}

	protected CombinerPredicate(String operator, List<SqlPredicate> predicates) {
		super(operator, new ArrayList<>());
		query.addAll(predicates);
	}

	@Override
	public String getSqlString() {
		return query.stream().map(p -> " ( " + p.getSqlString() + " ) ").collect(Collectors.joining(format, " ( ", " ) "));
	}

	public CombinerPredicate addPredicate(SqlPredicate predicate) {
		query.add(predicate);
		return this;
	}
}
