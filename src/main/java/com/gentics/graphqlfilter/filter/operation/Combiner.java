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

	private final String initiatingFilterName;
	private final String operator;
	private final List<FilterOperation<?>> operands;
	private final boolean unary;

	/**
	 * Constructor.
	 * 
	 * @param operator 
	 * @param operands
	 */
	public Combiner(String operator, List<FilterOperation<?>> operands, String initiatingFilterName) {
		this(operator, operands, false, initiatingFilterName);
	}
	/**
	 * Constructor.
	 * 
	 * @param operator 
	 * @param operands
	 */
	public Combiner(String operator, List<FilterOperation<?>> operands, boolean unary, String initiatingFilterName) {
		this.operator = operator;
		this.operands = operands;
		this.unary = unary;
		this.initiatingFilterName = initiatingFilterName;
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
	public boolean isUnary() {
		return unary;
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
	public static final Combiner and(List<FilterOperation<?>> operands, String initiatingFilterName) {
		return new Combiner("AND", operands, initiatingFilterName);
	}

	/**
	 * The logical OR operation.
	 * 
	 * @param operands
	 * @return
	 */
	public static final Combiner or(List<FilterOperation<?>> operands, String initiatingFilterName) {
		return new Combiner("OR", operands, initiatingFilterName);
	}

	/**
	 * The logical NOT operation.
	 * 
	 * @param operand
	 * @return
	 */
	public static final Combiner not(FilterOperation<?> operand, String initiatingFilterName) {
		return new Combiner("NOT", Arrays.asList(FilterOperation.noOp(), operand), true, initiatingFilterName);
	}

	@Override
	public String toString() {
		return "Combiner [operator=" + operator + ", operands=" + operands + "]";
	}
	@Override
	public String getInitiatingFilterName() {
		return initiatingFilterName;
	}
}
