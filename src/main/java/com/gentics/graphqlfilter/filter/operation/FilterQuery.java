package com.gentics.graphqlfilter.filter.operation;

import java.util.Optional;

public class FilterQuery<O, Q> {

	private final O owner;
	private final Q query;
	private final String field;

	public FilterQuery(O owner, String field, Q query) {
		this.owner = owner;
		this.field = field;
		this.query = query;
	}

	public O getOwner() {
		return owner;
	}

	public String getField() {
		return field;
	}

	public Q getQuery() {
		return query;
	}

	public FieldOperand<O> makeFieldOperand(Optional<String> alias) {
		return new FieldOperand<>(owner, field, alias);
	}

	public LiteralOperand<Q> makeValueOperand(boolean escape) {
		return new LiteralOperand<>(query, escape);
	}

	@Override
	public String toString() {
		return "FilterQuery [owner=" + owner + ", query=" + query + ", field=" + field + "]";
	}
}
