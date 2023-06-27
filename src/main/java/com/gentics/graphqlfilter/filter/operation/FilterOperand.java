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
	 * Is this operand a literal?
	 * 
	 * @return
	 */
	boolean isLiteral();

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

	static FilterOperand<?> noOp() {
		return new FilterOperand<String>() {

			@Override
			public String toSql() {
				return "";
			}

			@Override
			public Set<Join> getJoins(Set<Join> parent) {
				return Collections.emptySet();
			}

			@Override
			public String getValue() {
				return "";
			}

			@Override
			public boolean isLiteral() {
				return false;
			}

			@Override
			public Optional<String> maybeGetOwner() {
				return Optional.empty();
			}
		};
	}
}
