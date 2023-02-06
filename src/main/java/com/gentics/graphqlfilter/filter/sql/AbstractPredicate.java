package com.gentics.graphqlfilter.filter.sql;

public abstract class AbstractPredicate<Q> implements SqlPredicate {

	protected final String format;
	protected final Q query;

	public AbstractPredicate(String format, Q query) {
		super();
		this.format = format;
		this.query = query;
	}

	@Override
	public String getSqlString() {
		return String.format(format, query.toString());
	}

	public Q getQuery() {
		return query;
	}
}
