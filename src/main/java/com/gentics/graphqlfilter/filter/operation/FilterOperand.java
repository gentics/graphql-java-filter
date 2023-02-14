package com.gentics.graphqlfilter.filter.operation;

import java.util.Collections;
import java.util.Map;

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

	/**
	 * Is this operand a field name?
	 * 
	 * @return
	 */
	boolean isFieldName();

	/**
	 * Does this operand consider a join?
	 * 
	 * @return
	 */
	default Map<String, String> getJoins() {
		return getJoins(Collections.emptyMap());
	}
}
