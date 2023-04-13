package com.gentics.graphqlfilter.filter.operation;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

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
	 * Get an owner type if available.
	 * 
	 * @return
	 */
	Optional<String> maybeGetOwner();

	/**
	 * Does this operand consider a join?
	 * 
	 * @return
	 */
	default Set<Join> getJoins() {
		return getJoins(Collections.emptySet());
	}
}
