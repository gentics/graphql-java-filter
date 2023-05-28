package com.gentics.graphqlfilter.filter.operation;

import java.util.Collections;
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
	 * Get name of a filter that initiated this operation.
	 * 
	 * @return
	 */
	String getInitiatingFilterName();

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

	/**
	 * Is this a combination of operations?
	 * 
	 * @return
	 */
	default Optional<List<FilterOperation<?>>> maybeCombination() {
		return Optional.empty();
	}

	/**
	 * Is this a comparison operation?
	 * 
	 * @return
	 */
	default Optional<Pair<FilterOperand<?>, FilterOperand<?>>> maybeComparison() {
		return Optional.empty();
	}

	/**
	 * Should a representation of this operation be wrapped in "()" brackets for syntax secure?
	 * 
	 * @return
	 */
	default boolean shouldBracket() {
		return true;
	}

	/**
	 * Does this operation have only one operand?
	 * 
	 * @return
	 */
	default boolean isUnary() {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	default Set<Join> getJoins(Set<Join> parent) {
		return getOperands().stream().map(o -> o.getJoins(parent)).reduce(Set.class.cast(new HashSet<>()), (map, o) -> FilterUtil.addFluent(map, o), (a, b) -> a);
	}

	/**
	 * Get a filter's ID, if available.
	 * 
	 * @return
	 */
	default Optional<String> maybeGetFilterId() {
		return Optional.empty();
	}

	/**
	 * Set a filter's ID, if supported, NOP otherwise. Returns self.
	 * 
	 * @param maybeId
	 */
	default FilterOperation<T> maybeSetFilterId(Optional<String> maybeId) {
		return this;
	}

	/**
	 * An empty NOOP operation.
	 * 
	 * @return
	 */
	public static FilterOperation<?> noOp() {
		return new FilterOperation<Sqlable>() {

			@Override
			public String toSql() {
				return "";
			}

			@Override
			public String getOperator() {
				return "";
			}

			@Override
			public List<Sqlable> getOperands() {
				return Collections.emptyList();
			}

			@Override
			public boolean shouldBracket() {
				return false;
			}

			@Override
			public String getInitiatingFilterName() {
				return "";
			}
		};
	}
}
