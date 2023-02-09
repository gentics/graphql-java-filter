package com.gentics.graphqlfilter.filter.operation;

/**
 * This is thrown, if the filter query cannot be formalized. 
 * 
 * @author plyhun
 *
 */
public class UnformalizableQuery extends Exception {

	private static final long serialVersionUID = -4264961790054991138L;

	/**
	 * Inform the caller of the reasons of the query being unformalizable.
	 * 
	 * @param message
	 */
	public UnformalizableQuery(String message) {
		super(message);
	}
}
