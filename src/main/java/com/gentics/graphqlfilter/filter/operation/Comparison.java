package com.gentics.graphqlfilter.filter.operation;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A generic comparison operation implementation.
 * 
 * @author plyhun
 *
 */
public class Comparison implements ComparisonOperation {

	private final String operator;
	private final FilterOperand<?> left;
	private final FilterOperand<?> right;

	/**
	 * Constructor.
	 * 
	 * @param operator
	 * @param left
	 * @param right
	 */
	public Comparison(String operator, FilterOperand<?> left, FilterOperand<?> right) {
		this.operator = operator;
		this.left = left;
		this.right = right;
	}

	@Override
	public String getOperator() {
		return operator;
	}

	@Override
	public List<FilterOperand<?>> getOperands() {
		return Arrays.asList(left, right);
	}

	@Override
	public FilterOperand<?> getLeft() {
		return left;
	}

	@Override
	public FilterOperand<?> getRight() {
		return right;
	}

	/**
	 * Equality `=` comparison.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static final Comparison eq(FilterOperand<?> left, FilterOperand<?> right) {
		return new Comparison("=", left, right);
	}

	/**
	 * Likeness comparison.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static final Comparison like(FilterOperand<?> left, FilterOperand<?> right) {
		return new Comparison("LIKE", left, right);
	}

	/**
	 * Inequality `<>` comparison.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static final Comparison ne(FilterOperand<?> left, FilterOperand<?> right) {
		return new Comparison("<>", left, right);
	}

	/**
	 * Greater-than `>` operation.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static final Comparison gt(FilterOperand<?> left, FilterOperand<?> right) {
		return new Comparison(">", left, right);
	}

	/**
	 * Less-than `<` operation.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static final Comparison lt(FilterOperand<?> left, FilterOperand<?> right) {
		return new Comparison("<", left, right);
	}

	/**
	 * Greater-or-equal `>=` operation.
	 * @param left
	 * @param right
	 * @return
	 */
	public static final Comparison gte(FilterOperand<?> left, FilterOperand<?> right) {
		return new Comparison(">=", left, right);
	}

	/**
	 * Less-or-equal `<=` operation.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static final Comparison lte(FilterOperand<?> left, FilterOperand<?> right) {
		return new Comparison("<=", left, right);
	}

	/**
	 * Inclusion check operation.
	 * 
	 * @param left what should be checked for being included
	 * @param right what are the set to check for inclusion against
	 * @return
	 */
	public static final Comparison in(FilterOperand<?> left, LiteralOperand<? extends Collection<?>> right) {
		return new Comparison("IN", left, right) {
			@Override
			public String toSql() {
				return String.format(" ( %s %s [ %s ] ) ", getLeft().toSql(), getOperator(), getRight().toSql());
			}
		};
	}

	/**
	 * Null check operation.
	 * 
	 * @param left
	 * @return
	 */
	public static final Comparison isNull(FilterOperand<?> left) {
		return new Comparison("IS", left, new LiteralOperand<>("NULL", false));
	}

	/**
	 * Non-Null check operation.
	 * 
	 * @param left
	 * @return
	 */
	public static final Comparison isNotNull(FilterOperand<?> left) {
		return new Comparison("IS NOT", left, new LiteralOperand<>("NULL", false));
	}

	/**
	 * Null check operation.
	 * 
	 * @param left
	 * @return
	 */
	public static final Comparison dummy(boolean shouldSucceed) {
		return new Comparison(shouldSucceed ? "=" : "<>", new LiteralOperand<>(1, false), new LiteralOperand<>(1, false));
	}

	@Override
	public String toString() {
		return "Comparison [operator=" + operator + ", left=" + left + ", right=" + right + "]";
	}
}
