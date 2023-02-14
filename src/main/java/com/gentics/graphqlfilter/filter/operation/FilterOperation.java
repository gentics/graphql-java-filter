package com.gentics.graphqlfilter.filter.operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	default Map<String, String> getJoins(Map<String, String> parent) {
		return getOperands().stream().map(o -> o.getJoins(parent)).reduce(Map.class.cast(new HashMap<>()), (map, o) -> FilterUtil.addFluent(map, o), (a, b) -> a);
	}
}
