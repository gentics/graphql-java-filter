package com.gentics.graphqlfilter.filter.operation;

import java.util.Collection;
import java.util.Optional;

/**
 * A generic comparison operation implementation.
 * 
 * @author plyhun
 *
 */
public class Comparison extends AbstractOperation<FilterOperand<?>> implements ComparisonOperation {

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
	public Comparison(String operator, FilterOperand<?> left, FilterOperand<?> right, String initiatingFilterName) {
		this(operator, left, right, initiatingFilterName, Optional.empty());
	}

	public Comparison(String operator, FilterOperand<?> left, FilterOperand<?> right, String initiatingFilterName, Optional<String> maybeId) {
		super(maybeId, initiatingFilterName);
		this.operator = operator;
		this.left = left;
		this.right = right;
	}

	@Override
	public String getOperator() {
		return operator;
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
	public static final Comparison eq(FilterOperand<?> left, FilterOperand<?> right, String initiatingFilterName) {
		return new Comparison("=", left, right, initiatingFilterName);
	}

	/**
	 * Likeness comparison.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static final Comparison like(FilterOperand<?> left, FilterOperand<?> right, String initiatingFilterName) {
		return new Comparison("LIKE", left, right, initiatingFilterName);
	}

	/**
	 * Inequality `<>` comparison.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static final Comparison ne(FilterOperand<?> left, FilterOperand<?> right, String initiatingFilterName) {
		return new Comparison("<>", left, right, initiatingFilterName);
	}

	/**
	 * Greater-than `>` operation.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static final Comparison gt(FilterOperand<?> left, FilterOperand<?> right, String initiatingFilterName) {
		return new Comparison(">", left, right, initiatingFilterName);
	}

	/**
	 * Less-than `<` operation.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static final Comparison lt(FilterOperand<?> left, FilterOperand<?> right, String initiatingFilterName) {
		return new Comparison("<", left, right, initiatingFilterName);
	}

	/**
	 * Greater-or-equal `>=` operation.
	 * @param left
	 * @param right
	 * @return
	 */
	public static final Comparison gte(FilterOperand<?> left, FilterOperand<?> right, String initiatingFilterName) {
		return new Comparison(">=", left, right, initiatingFilterName);
	}

	/**
	 * Less-or-equal `<=` operation.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static final Comparison lte(FilterOperand<?> left, FilterOperand<?> right, String initiatingFilterName) {
		return new Comparison("<=", left, right, initiatingFilterName);
	}

	/**
	 * Inclusion check operation.
	 * 
	 * @param left what should be checked for being included
	 * @param right what are the set to check for inclusion against
	 * @return
	 */
	public static final Comparison in(FilterOperand<?> left, LiteralOperand<? extends Collection<?>> right, String initiatingFilterName) {
		return new Comparison("IN", left, right, initiatingFilterName) {
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
	public static final Comparison isNull(FilterOperand<?> left, String initiatingFilterName) {
		return new Comparison("IS", left, new LiteralOperand<>("NULL", false), initiatingFilterName);
	}

	/**
	 * Non-Null check operation.
	 * 
	 * @param left
	 * @return
	 */
	public static final Comparison isNotNull(FilterOperand<?> left, String initiatingFilterName) {
		return new Comparison("IS NOT", left, new LiteralOperand<>("NULL", false), initiatingFilterName);
	}

	/**
	 * Null check operation.
	 * 
	 * @param left
	 * @return
	 */
	public static final Comparison dummy(boolean shouldSucceed, String initiatingFilterName) {
		return new Comparison(shouldSucceed ? "=" : "<>", new LiteralOperand<>(1, false), new LiteralOperand<>(1, false), initiatingFilterName);
	}

	@Override
	public String toString() {
		return "Comparison [operator=" + operator + ", left=" + left + ", right=" + right + "]";
	}
}
