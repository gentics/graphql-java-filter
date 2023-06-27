package com.gentics.graphqlfilter.filter.operation;

/**
 * This is thrown, if the filter query cannot be formalized. 
 * 
 * @author plyhun
 *
 */
public class UnformalizableQuery extends Exception {

	private static final long serialVersionUID = -4264961790054991138L;
	private final String filter;

	/**
	 * Inform the caller of the reasons of the query being unformalizable.
	 * 
	 * @param message
	 */
	public UnformalizableQuery(String filter, String message) {
		super(message);
		this.filter = filter;
	}

	public String getFilter() {
		return filter;
	}
}
