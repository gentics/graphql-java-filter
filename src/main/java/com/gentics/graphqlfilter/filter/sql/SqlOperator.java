package com.gentics.graphqlfilter.filter.sql;

public enum SqlOperator {

	AND(" ( %s ) AND ( %s ) "),
	OR(" ( %s ) OR ( %s ) "),
	NOT(" NOT( %s ) "),
	EQ(" %s = %s "),
	NOTEQ(" %s <> %s "),
	IN(" %s IN ( %s ) "),
	ISNULL(" %s IS NULL ");

	private final String format;

	private SqlOperator(String format) {
		this.format = format;
	}

	@SuppressWarnings("unchecked")
	public <T> String makeSql(T... operands) {
		return String.format(format, operands);
	}
}
