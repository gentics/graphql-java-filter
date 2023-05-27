package com.gentics.graphqlfilter.filter;

import static com.gentics.graphqlfilter.util.FilterUtil.nullablePredicate;
import static graphql.Scalars.GraphQLString;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.gentics.graphqlfilter.filter.operation.Comparison;

import graphql.schema.GraphQLList;

/**
 * Filters strings by various means
 */
public class StringFilter extends MainFilter<String> {

	private static final String UNDERSCORE_PLACEHOLDER = "\\{UNS}\\";
	private static final String PERCENT_PLACEHOLDER = "\\{REM}\\";
	private static final String DOT_PLACEHOLDER = "\\{DOT}\\";
	private static StringFilter instance;

	/**
	 * Get the singleton string filter
	 */
	public static synchronized StringFilter filter() {
		if (instance == null) {
			instance = new StringFilter(Optional.empty());
		}
		return instance;
	}

	private StringFilter(Optional<String> ownerType) {
		super("StringFilter", "Filters Strings", false, ownerType);
	}

	@Override
	protected List<FilterField<String, ?>> getFilters() {
		return Arrays.asList(
			FilterField.isNull(),
			FilterField.create("equals", "Compares two strings for equality", GraphQLString, 
					query -> query::equals, 
					Optional.of((query) -> Comparison.eq(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(true), query.getInitiatingFilterName()))),
			FilterField.create("notEquals", "Compares two strings for equality", GraphQLString, 
					query -> value -> !query.equals(value), 
					Optional.of((query) -> Comparison.ne(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(true), query.getInitiatingFilterName()))),
			FilterField.<String, String>create("contains", "Checks if the string contains the given substring.", GraphQLString, 
					query -> nullablePredicate(input -> input.contains(query)),
					Optional.empty()),
			FilterField.<String, List<String>>create("oneOf", "Checks if the string is equal to one of the given strings", GraphQLList.list(GraphQLString), 
					query -> query::contains,
					Optional.of((query) -> Comparison.in(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(true), query.getInitiatingFilterName()))),
			FilterField.<String, String>create("regex", "Checks if the string matches the given regular expression.", GraphQLString, 
					query -> nullablePredicate(Pattern.compile(query).asPredicate()),
					Optional.empty()),
			FilterField.<String, String>create("like", "Checks if the string matches the given SQL LIKE expression.", GraphQLString, 
					query -> nullablePredicate(Pattern.compile(likeToRegex(query)).asPredicate()),
					Optional.of((query) -> Comparison.like(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(true), query.getInitiatingFilterName()))));
	}

	private String likeToRegex(String likeQuery) {
		return likeQuery
				.replace("\\.", DOT_PLACEHOLDER).replace("\\%", PERCENT_PLACEHOLDER).replace("\\_", UNDERSCORE_PLACEHOLDER)
				.replace(".", "\\.").replace("%", ".*?").replace("_", ".")
				.replace(DOT_PLACEHOLDER, ".").replace(PERCENT_PLACEHOLDER, "%").replace(UNDERSCORE_PLACEHOLDER, "_");
	}
}
