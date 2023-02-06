package com.gentics.graphqlfilter.filter.sql;

public class ComparisonPredicate<Q> extends AbstractPredicate<Q> {

	protected final String field;
	protected final String operator;

	public ComparisonPredicate(String operator, String field, Q query, boolean escape) {
		this(escape ? " ( %s %s '%s' ) " : " ( %s %s %s ) ", operator, field, query);
	}

	protected ComparisonPredicate(String format, String operator, String field, Q query) {
		super(format, query);
		this.field = field;
		this.operator = operator;
	}

	@Override
	public String getSqlString() {
		return String.format(format, field, operator, query.toString());
	}

	public String getField() {
		return field;
	}

	public String getOperator() {
		return operator;
	}
}
