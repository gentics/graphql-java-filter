package com.gentics.graphqlfilter.filter.operation;

import java.util.Map;

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
	Map<String, String> getJoins(Map<String, String> parent);
}
