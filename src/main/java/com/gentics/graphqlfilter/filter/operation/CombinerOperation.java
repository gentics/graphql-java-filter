package com.gentics.graphqlfilter.filter.operation;

import java.util.List;
import java.util.Optional;

/**
 * A formalized representation of an operation that combines several operation in a certain manner.
 * 
 * @author plyhun
 *
 */
public interface CombinerOperation extends FilterOperation<FilterOperation<?>> {

	@Override
	default Optional<List<FilterOperation<?>>> maybeCombination() {
		return Optional.of(getOperands());
	}
}
