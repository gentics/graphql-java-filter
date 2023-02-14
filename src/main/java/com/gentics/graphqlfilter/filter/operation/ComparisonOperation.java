package com.gentics.graphqlfilter.filter.operation;

import java.util.Optional;

import graphql.util.Pair;

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
	default Optional<Pair<FilterOperand<?>, FilterOperand<?>>> maybeComparison() {
		return Optional.of(Pair.pair(getLeft(), getRight()));
	}

	@Override
	default String toSql() {
		return String.format(SQL_FORMAT, getLeft().toSql(), getOperator(), getRight().toSql());
	}
}
