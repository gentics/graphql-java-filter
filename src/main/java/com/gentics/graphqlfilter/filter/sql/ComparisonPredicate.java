package com.gentics.graphqlfilter.filter.sql;

import java.util.List;
import java.util.stream.Collectors;

public class ComparisonPredicate<Q> extends AbstractPredicate<Q> {

	protected final List<SqlField<?>> fields;
	protected final String operator;

	public ComparisonPredicate(String operator, List<SqlField<?>> fields, Q query, boolean escape) {
		this(escape ? " ( %s %s '%s' ) " : " ( %s %s %s ) ", operator, fields, query);
	}

	protected ComparisonPredicate(String format, String operator, List<SqlField<?>> fields, Q query) {
		super(format, query);
		this.fields = fields;
		this.operator = operator;
	}

	@Override
	public String getSqlString() {
		return String.format(format, fields.stream().map(SqlField::getName).collect(Collectors.joining(".")), operator, String.valueOf(query));
	}

	public List<SqlField<?>> getFields() {
		return fields;
	}

	public String getOperator() {
		return operator;
	}
}
