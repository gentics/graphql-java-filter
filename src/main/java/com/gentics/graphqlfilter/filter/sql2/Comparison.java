package com.gentics.graphqlfilter.filter.sql2;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Comparison implements ComparisonOperation {

	private final String operator;
	private final FilterOperand<?> left;
	private final FilterOperand<?> right;

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

	public static final Comparison eq(FilterOperand<?> left, FilterOperand<?> right) {
		return new Comparison("=", left, right);
	}
	public static final Comparison ne(FilterOperand<?> left, FilterOperand<?> right) {
		return new Comparison("<>", left, right);
	}
	public static final Comparison gt(FilterOperand<?> left, FilterOperand<?> right) {
		return new Comparison(">", left, right);
	}
	public static final Comparison lt(FilterOperand<?> left, FilterOperand<?> right) {
		return new Comparison("<", left, right);
	}
	public static final Comparison gte(FilterOperand<?> left, FilterOperand<?> right) {
		return new Comparison(">=", left, right);
	}
	public static final Comparison lte(FilterOperand<?> left, FilterOperand<?> right) {
		return new Comparison("<=", left, right);
	}
	public static final Comparison in(FilterOperand<?> left, LiteralOperand<? extends Collection<?>> right) {
		return new Comparison("IN", left, right) {
			@Override
			public String toSql() {
				return String.format(" ( %s %s ( %s ) ) ", getLeft().toSql(), getOperator(), right.getValue().stream().map(String::valueOf).collect(Collectors.joining(",")));
			}
		};
	}
	public static final Comparison isNull(FilterOperand<?> left) {
		return new Comparison("IS", left, new LiteralOperand<>("NULL", false));
	}

	@Override
	public String toString() {
		return "Comparison [operator=" + operator + ", left=" + left + ", right=" + right + "]";
	}
}
