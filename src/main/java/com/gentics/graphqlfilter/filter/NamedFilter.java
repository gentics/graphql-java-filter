package com.gentics.graphqlfilter.filter;

/**
 * Strictly named filter.
 * 
 * @author plyhun
 *
 * @param <T>
 *            The type of items that will be filtered
 * @param <Q>
 *            The Java representation of the GraphQLInputType of this query.
 */
public interface NamedFilter<T, Q> extends Filter<T, Q> {

	/**
	 * Get filter name.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Get sort filter name. 
	 * 
	 * @return
	 */
	default String getSortingName() {
		return getName() + "Sort";
	}
}
