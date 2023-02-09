package com.gentics.graphqlfilter.filter.operation;

/**
 * A comparison operation over a pair of operands of an arbitrary type.
 * 
 * @author plyhun
 *
 */
public interface ComparisonOperation extends FilterOperation<FilterOperand<?>> {
	public static final String SQL_FORMAT = " ( %s %s %s ) ";

	/**
	 * Get left operand.
	 * 
	 * @return
	 */
	FilterOperand<?> getLeft();

	/**
	 * Get right operand.
	 * 
	 * @return
	 */
	FilterOperand<?> getRight();

	@Override
	default String toSql() {
		return String.format(SQL_FORMAT, getLeft().toSql(), getOperator(), getRight().toSql());
	}
}
