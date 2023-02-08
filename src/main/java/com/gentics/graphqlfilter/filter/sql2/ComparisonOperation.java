package com.gentics.graphqlfilter.filter.sql2;

public interface ComparisonOperation extends FilterOperation<FilterOperand<?>> {
	public static final String SQL_FORMAT = " ( %s %s %s ) ";

	FilterOperand<?> getLeft();
	FilterOperand<?> getRight();

	@Override
	default String toSql() {
		return String.format(SQL_FORMAT, getLeft().toSql(), getOperator(), getRight().toSql());
	}
}
