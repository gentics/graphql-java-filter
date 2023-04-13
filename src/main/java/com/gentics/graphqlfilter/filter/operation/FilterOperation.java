package com.gentics.graphqlfilter.filter.operation;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.gentics.graphqlfilter.util.FilterUtil;

import graphql.util.Pair;

/**
 * A formalized representation of a filtering operation.
 * 
 * @author plyhun
 *
 * @param <T> operands type
 */
public interface FilterOperation<T extends Sqlable> extends Sqlable {

	/**
	 * Get an operator.
	 * 
	 * @return
	 */
	String getOperator();

	/**
	 * Get operands.
	 * 
	 * @return
	 */
	List<T> getOperands();

	default Optional<List<FilterOperation<?>>> maybeCombination() {
		return Optional.empty();
	}

	default Optional<Pair<FilterOperand<?>, FilterOperand<?>>> maybeComparison() {
		return Optional.empty();
	}

	@SuppressWarnings("unchecked")
	@Override
	default Set<Join> getJoins(Set<Join> parent) {
		return getOperands().stream().map(o -> o.getJoins(parent)).reduce(Set.class.cast(new HashSet<>()), (map, o) -> FilterUtil.addFluent(map, o), (a, b) -> a);
	}
}
