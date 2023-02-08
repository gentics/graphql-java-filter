package com.gentics.graphqlfilter.filter.operation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Combiner implements CombinerOperation {

	private final String operator;
	private final List<FilterOperation<?>> operands;

	public Combiner(String operator, List<FilterOperation<?>> operands) {
		this.operator = operator;
		this.operands = operands;
	}

	@Override
	public String getOperator() {
		return operator;
	}

	@Override
	public List<FilterOperation<?>> getOperands() {
		return operands;
	}

	@Override
	public String toSql() {
		return operands.stream().map(Sqlable::toSql).collect(Collectors.joining(" ) " + operator + " ( ", " ( ", " ) "));
	}

	public static final Combiner and(List<FilterOperation<?>> operands) {
		return new Combiner("AND", operands);
	}
	public static final Combiner or(List<FilterOperation<?>> operands) {
		return new Combiner("OR", operands);
	}
	public static final Combiner not(FilterOperation<?> operand) {
		return new Combiner("NOT", Arrays.asList(operand));
	}

	@Override
	public String toString() {
		return "Combiner [operator=" + operator + ", operands=" + operands + "]";
	}

}
