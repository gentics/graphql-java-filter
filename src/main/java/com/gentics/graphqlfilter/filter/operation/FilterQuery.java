package com.gentics.graphqlfilter.filter.operation;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * A representation of not yet parsed GraphQL query, to use in the formalization mechanism.
 * 
 * @author plyhun
 *
 * @param <O> owner type
 * @param <Q> query type
 */
public class FilterQuery<O, Q> {

	private final O owner;
	private final Q query;
	private final String field;
	private final Optional<Set<Join>> maybeJoins;

	public FilterQuery(O owner, String field, Q query, Optional<Set<Join>> maybeJoins) {
		this.owner = owner;
		this.field = field;
		this.query = query;
		this.maybeJoins = maybeJoins;
	}

	public FilterQuery(O owner, String field, Q query) {
		this(owner, field, query, Optional.empty());
	}

	/**
	 * Get an owner type.
	 * 
	 * @return
	 */
	public O getOwner() {
		return owner;
	}

	/**
	 * Get the field name.
	 * 
	 * @return
	 */
	public String getField() {
		return field;
	}

	/**
	 * Get the raw query.
	 * 
	 * @return
	 */
	public Q getQuery() {
		return query;
	}

	/**
	 * Make a field operand out of this query.
	 * 
	 * @param alias
	 * @return
	 */
	public FieldOperand<O> makeFieldOperand(Optional<String> alias) {
		return new FieldOperand<>(owner, field, maybeJoins, alias);
	}

	/**
	 * Make a literal operand out of this query.
	 * 
	 * @param escape
	 * @return
	 */
	public LiteralOperand<Q> makeValueOperand(boolean escape) {
		return new LiteralOperand<>(query, escape);
	}

	/**
	 * Make a literal operand out of this query.
	 * 
	 * @param escape
	 * @return
	 */
	public <T> LiteralOperand<T> makeValueOperand(boolean escape, Function<Q, T> adapter) {
		return new LiteralOperand<>(adapter.apply(query), escape);
	}

	/**
	 * Get the joins between owner, if available.
	 * 
	 * @return
	 */
	public Optional<Set<Join>> getMaybeJoins() {
		return maybeJoins;
	}

	@Override
	public String toString() {
		return "FilterQuery [owner=" + owner + ", query=" + query + ", field=" + field + ", maybeJoins=" + maybeJoins + "]";
	}
}
