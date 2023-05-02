package com.gentics.graphqlfilter.filter.operation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A generic implementation of {@link CombinerOperation}.
 * 
 * @author plyhun
 *
 */
public class Combiner implements CombinerOperation {

	private final String operator;
	private final List<FilterOperation<?>> operands;

	/**
	 * Constructor.
	 * 
	 * @param operator 
	 * @param operands
	 */
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

	/**
	 * The logical AND operation.
	 * 
	 * @param operands
	 * @return
	 */
	public static final Combiner and(List<FilterOperation<?>> operands) {
		return new Combiner("AND", operands);
	}

	/**
	 * The logical OR operation.
	 * 
	 * @param operands
	 * @return
	 */
	public static final Combiner or(List<FilterOperation<?>> operands) {
		return new Combiner("OR", operands);
	}

	/**
	 * The logical NOT operation.
	 * 
	 * @param operand
	 * @return
	 */
	public static final Combiner not(FilterOperation<?> operand) {
		return new Combiner("NOT", Arrays.asList(FilterOperation.noOp(), operand));
	}

	@Override
	public String toString() {
		return "Combiner [operator=" + operator + ", operands=" + operands + "]";
	}
}
