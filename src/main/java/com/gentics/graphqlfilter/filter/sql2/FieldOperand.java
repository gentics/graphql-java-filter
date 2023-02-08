package com.gentics.graphqlfilter.filter.sql2;

import java.util.Optional;

public class FieldOperand<T> implements FilterOperand<String> {

	private final T owner;
	private final String field;
	private final Optional<String> alias;

	public FieldOperand(T etype, String field) {
		this(etype, field, Optional.empty());
	}

	public FieldOperand(T owner, String field, Optional<String> alias) {
		this.owner = owner;
		this.field = field;
		this.alias = alias;
	}

	@Override
	public String getValue() {
		return field;
	}

	@Override
	public String toSql() {
		return alias.orElse("_" + String.valueOf(owner).toLowerCase()) + "." + field;
	}

	@Override
	public String toString() {
		return "FieldOperand [owner=" + owner + ", field=" + field + ", alias=" + alias + "]";
	}

	public T getOwner() {
		return owner;
	}
}
