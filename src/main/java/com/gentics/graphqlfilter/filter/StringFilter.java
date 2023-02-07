package com.gentics.graphqlfilter.filter;

import static com.gentics.graphqlfilter.util.FilterUtil.nullablePredicate;
import static graphql.Scalars.GraphQLString;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.gentics.graphqlfilter.filter.sql.ComparisonPredicate;
import com.gentics.graphqlfilter.filter.sql.InPredicate;

import graphql.schema.GraphQLList;

/**
 * Filters strings by various means
 */
public class StringFilter extends MainFilter<String> {

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
					Optional.of((query, fields) -> new ComparisonPredicate<>("=", fields, query, true))),
			FilterField.<String, String>create("contains", "Checks if the string contains the given substring.", GraphQLString, 
					query -> nullablePredicate(input -> input.contains(query)),
					Optional.empty()),
			FilterField.<String, List<String>>create("oneOf", "Checks if the string is equal to one of the given strings", GraphQLList.list(GraphQLString), 
					query -> query::contains,
					Optional.of((query, fields) -> new InPredicate(fields, query, true))),
			FilterField.<String, String>create("regex", "Checks if the string matches the given regular expression.", GraphQLString, 
					query -> nullablePredicate(Pattern.compile(query).asPredicate()),
					Optional.empty()));
	}
}
