package com.gentics.graphqlfilter.filter.operation;

import graphql.com.google.common.base.Objects;

/**
 * A part of table/entity join, consisting of table name and field.
 * 
 * @author plyhun
 *
 */
public class JoinPart {

	private final String table;
	private final String field;

	public JoinPart(String table, String field) {
		this.table = table;
		this.field = field;
	}

	public String getTable() {
		return table;
	}

	public String getField() {
		return field;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(table, field);
	}

	@Override
	public boolean equals(Object obj) {
		if (JoinPart.class.isInstance(obj)) {
			return Objects.equal(table, ((JoinPart) obj).table) && Objects.equal(field, ((JoinPart) obj).field);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "JoinPart [table=" + table + ", field=" + field + "]";
	}

	public String toSql() {
		return table + "." + field;
	}
}
