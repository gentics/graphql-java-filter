package com.gentics.graphqlfilter.filter.sql;

import java.util.Optional;

public class SqlFilter<T, C> {
	
	protected final SqlOperator operator;
	protected final T operand;
	protected final boolean needsEscape;
	protected final String field;
	protected final Class<C> classOfTable;
	protected Optional<String> tableAlias = Optional.empty();

	public SqlFilter(SqlOperator operator, T operand, boolean needsEscape, String field, Class<C> classOfTable,
			Optional<String> tableAlias) {
		super();
		this.operator = operator;
		this.operand = operand;
		this.needsEscape = needsEscape;
		this.field = field;
		this.classOfTable = classOfTable;
		this.tableAlias = tableAlias;
	}

	public Optional<String> getTableAlias() {
		return tableAlias;
	}

	public void setTableAlias(Optional<String> tableAlias) {
		this.tableAlias = tableAlias;
	}

	public SqlOperator getOperator() {
		return operator;
	}

	public T getOperand() {
		return operand;
	}

	public boolean isNeedsEscape() {
		return needsEscape;
	}

	public String getField() {
		return field;
	}

	public Class<C> getClassOfTable() {
		return classOfTable;
	}

	public String makeSql() {
		
	}
}
