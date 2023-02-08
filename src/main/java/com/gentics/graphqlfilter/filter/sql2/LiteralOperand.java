package com.gentics.graphqlfilter.filter.sql2;

public class LiteralOperand<T> implements FilterOperand<T> {

	private final boolean escape;
	private final T value;

	public LiteralOperand(T value, boolean escape) {
		this.escape = escape;
		this.value = value;
	}

	@Override
	public String toSql() {
		return escape ? String.format("'%s'", value) : String.valueOf(value);
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "LiteralOperand [escape=" + escape + ", value=" + value + "]";
	}
}
