package com.gentics.graphqlfilter.filter;

import static com.gentics.graphqlfilter.util.FilterUtil.nullablePredicate;
import static graphql.scalars.java.JavaPrimitives.GraphQLBigDecimal;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.filter.operation.Comparison;

import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLList;

/**
 * Filters strings by various means
 */
public class NumberFilter extends MainFilter<Number> {

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List<FilterField<Number, ?>> getFilters() {
		List<FilterField<? extends Number, ?>> filters = Arrays.asList(
			FilterField.isNull(),
			FilterField.<Number, Number>create("equals",
				"Compares two numbers for equality. Be careful when comparing floating point numbers, they might be not exact. In that case, use closeTo instead.",
				GraphQLBigDecimal, query -> val -> val != null && fromNumber(val).compareTo(fromNumber(query)) == 0,
				Optional.of(query -> Comparison.eq(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false)))),
			FilterField.<Number, Number>create("notEquals",
					"Compares two numbers for inequality. Be careful when comparing floating point numbers, they might be not exact. In that case, use closeTo instead.",
					GraphQLBigDecimal, query -> val -> val != null && fromNumber(val).compareTo(fromNumber(query)) != 0,
					Optional.of(query -> Comparison.ne(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false)))),
			FilterField.<Number, List<Number>>create("oneOf", "Tests if the number is equal to one of the given numbers",
				GraphQLList.list(GraphQLBigDecimal), query -> val -> val != null && query.stream().anyMatch(v -> fromNumber(val).compareTo(fromNumber(v)) == 0),
				Optional.of(query -> Comparison.in(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false)))),
			FilterField.<Number, Number>create("gt", "Tests if the number is greater than the given number",
				GraphQLBigDecimal, query -> val -> val != null && fromNumber(val).compareTo(fromNumber(query)) > 0,
				Optional.of(query -> Comparison.gt(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false)))),
			FilterField.<Number, Number>create("gte", "Tests if the number is greater than or equal to the given number",
				GraphQLBigDecimal, query -> val -> val != null && fromNumber(val).compareTo(fromNumber(query)) >= 0,
				Optional.of(query -> Comparison.gte(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false)))),
			FilterField.<Number, Number>create("lt", "Tests if the number is less than the given number",
				GraphQLBigDecimal, query -> val -> val != null && fromNumber(val).compareTo(fromNumber(query)) < 0,
				Optional.of(query -> Comparison.lt(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false)))),
			FilterField.<Number, Number>create("lte", "Tests if the number is less than or equal to the given number",
				GraphQLBigDecimal, query -> val -> val != null && fromNumber(val).compareTo(fromNumber(query)) <= 0,
				Optional.of(query -> Comparison.lte(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false)))),
			FilterField.<Number, Number>create("divisibleBy", "Tests if the number is divisible by the given number",
				GraphQLBigDecimal, query -> val -> val != null && fromNumber(val).remainder(fromNumber(query)).compareTo(BigDecimal.ZERO) == 0,
				Optional.empty()),
			FilterField.create("closeTo", "Tests if the number is close to the given number by a given error margin.",
				closeToType, NumberFilter::closeTo,
				Optional.empty()
			));
		return (List) filters;
	}

	public static BigDecimal fromNumber(Number number) {
		if (number instanceof BigDecimal) {
			return (BigDecimal) number;
		}
		if (number instanceof Integer
                || number instanceof Long
                || number instanceof Short
                || number instanceof Byte) {
            return BigDecimal.valueOf(number.longValue());
        }
        return BigDecimal.valueOf(number.doubleValue());
	}

	private static Predicate<BigDecimal> closeTo(Map<String, Number> query) {
		BigDecimal value = fromNumber(query.get("value"));
		BigDecimal maxDifference = fromNumber(query.get("maxDifference"));
		return nullablePredicate(val -> val.subtract(value).abs().compareTo(maxDifference) <= 0);
	}
}
