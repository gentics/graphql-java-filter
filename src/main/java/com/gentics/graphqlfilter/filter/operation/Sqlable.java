package com.gentics.graphqlfilter.filter.operation;

import java.util.Set;

/**
 * A formalized filter part that may be presented in a SQL-like form.
 * 
 * @author plyhun
 *
 */
public interface Sqlable {

	/**
	 * Get the SQL representation of a filter (WHERE conditions)
	 * 
	 * @return
	 */
	String toSql();

	/**
	 * Get the SQL joins representation, if available.
	 * 
	 * @param parent
	 * @return
	 */
	Set<Join> getJoins(Set<Join> parent);
}
