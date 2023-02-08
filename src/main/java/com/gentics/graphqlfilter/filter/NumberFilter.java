package com.gentics.graphqlfilter.filter;

import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLList;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.filter.sql.ComparisonPredicate;
import com.gentics.graphqlfilter.filter.sql.InPredicate;
import com.gentics.graphqlfilter.filter.sql2.Comparison;

import static com.gentics.graphqlfilter.util.FilterUtil.nullablePredicate;
import static graphql.scalars.java.JavaPrimitives.GraphQLBigDecimal;

/**
 * Filters strings by various means
 */
public class NumberFilter extends MainFilter<BigDecimal> {

	private static NumberFilter instance;

	/**
	 * Get the singleton string filter
	 */
	public static synchronized NumberFilter filter() {
		if (instance == null) {
			instance = new NumberFilter();
		}
		return instance;
	}

	private NumberFilter() {
		super("NumberFilter", "Filters numbers", false, Optional.empty());
	}

	private static GraphQLInputType closeToType = GraphQLInputObjectType.newInputObject()
		.name("CloseToFilter")
		.description("Tests if the number is close to the given number by a given error margin.")
		.field(GraphQLInputObjectField.newInputObjectField()
			.name("value")
			.description("The value to compare to")
			.type(GraphQLBigDecimal)
			.build())
		.field(GraphQLInputObjectField.newInputObjectField()
			.name("maxDifference")
			.description("The maximum allowed difference")
			.type(GraphQLBigDecimal)
			.build())
		.build();

	@Override
	protected List<FilterField<BigDecimal, ?>> getFilters() {
		return Arrays.asList(
			FilterField.isNull(),
			FilterField.<BigDecimal, BigDecimal>create("equals",
				"Compares two numbers for equality. Be careful when comparing floating point numbers, they might be not exact. In that case, use closeTo instead.",
				GraphQLBigDecimal, query -> nullablePredicate(val -> val.compareTo(query) == 0),
				Optional.of((query, fields) -> new ComparisonPredicate<>("=", fields, query, true)),
				Optional.of(query -> Comparison.eq(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false)))),
			FilterField.<BigDecimal, List<BigDecimal>>create("oneOf", "Tests if the number is equal to one of the given numbers",
				GraphQLList.list(GraphQLBigDecimal), query -> nullablePredicate(val -> query.stream().anyMatch(v -> v.compareTo(val) == 0)),
				Optional.of((query, fields) -> new InPredicate(fields, query, true)),
				Optional.of(query -> Comparison.in(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false)))),
			FilterField.<BigDecimal, BigDecimal>create("gt", "Tests if the number is greater than the given number",
				GraphQLBigDecimal, query -> nullablePredicate(val -> val.compareTo(query) > 0),
				Optional.of((query, fields) -> new ComparisonPredicate<>(">", fields, query, true)),
				Optional.of(query -> Comparison.gt(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false)))),
			FilterField.<BigDecimal, BigDecimal>create("gte", "Tests if the number is greater than or equal to the given number",
				GraphQLBigDecimal, query -> nullablePredicate(val -> val.compareTo(query) >= 0),
				Optional.of((query, fields) -> new ComparisonPredicate<>(">=", fields, query, true)),
				Optional.of(query -> Comparison.gte(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false)))),
			FilterField.<BigDecimal, BigDecimal>create("lt", "Tests if the number is less than the given number",
				GraphQLBigDecimal, query -> nullablePredicate(val -> val.compareTo(query) < 0),
				Optional.of((query, fields) -> new ComparisonPredicate<>("<", fields, query, true)),
				Optional.of(query -> Comparison.lt(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false)))),
			FilterField.<BigDecimal, BigDecimal>create("lte", "Tests if the number is less than or equal to the given number",
				GraphQLBigDecimal, query -> nullablePredicate(val -> val.compareTo(query) <= 0),
				Optional.of((query, fields) -> new ComparisonPredicate<>("<=", fields, query, true)),
				Optional.of(query -> Comparison.lte(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false)))),
			FilterField.<BigDecimal, BigDecimal>create("divisibleBy", "Tests if the number is divisible by the given number",
				GraphQLBigDecimal, query -> nullablePredicate(val -> val.remainder(query).compareTo(BigDecimal.ZERO) == 0),
				Optional.empty(), Optional.empty()),
			FilterField.create("closeTo", "Tests if the number is close to the given number by a given error margin.",
				closeToType, NumberFilter::closeTo,
				Optional.empty(), Optional.empty()
			));
	}

	private static Predicate<BigDecimal> closeTo(Map<String, BigDecimal> query) {
		BigDecimal value = query.get("value");
		BigDecimal maxDifference = query.get("maxDifference");
		return nullablePredicate(val -> val.subtract(value).abs().compareTo(maxDifference) <= 0);
	}
}
