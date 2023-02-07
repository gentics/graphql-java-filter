package com.gentics.graphqlfilter.filter.sql;

public class SqlField<T> {

	protected final String name;
	protected final T ftype;

	public SqlField(String name, T ftype) {
		this.name = name;
		this.ftype = ftype;
	}
	public String getName() {
		return name;
	}
	public T getFtype() {
		return ftype;
	}
}
