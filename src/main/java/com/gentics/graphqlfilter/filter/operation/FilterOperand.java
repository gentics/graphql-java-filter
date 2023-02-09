package com.gentics.graphqlfilter.filter.operation;

/**
 * A formal representation of a filter operand.
 * 
 * @author plyhun
 *
 * @param <T> operand type
 */
public interface FilterOperand<T> extends Sqlable {

	/**
	 * Get the actual operand value.
	 * 
	 * @return
	 */
	T getValue();
}
