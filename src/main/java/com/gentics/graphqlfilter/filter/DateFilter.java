package com.gentics.graphqlfilter.filter;

import static com.gentics.graphqlfilter.util.FilterUtil.nullablePredicate;
import static graphql.Scalars.GraphQLBoolean;
import static graphql.Scalars.GraphQLString;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.gentics.graphqlfilter.filter.operation.Comparison;
import com.gentics.graphqlfilter.filter.operation.LiteralOperand;

import graphql.schema.GraphQLList;

/**
 * Filters ISO-8601 strings by various means
 * 
 * TODO: Consider using Instant instead of Long as predicate input type
 */
public class DateFilter extends MainFilter<Long> {

	private static DateFilter instance;

	/**
	 * Filters ISO-8601 strings by various means
	 */
	public static synchronized DateFilter filter() {
		if (instance == null) {
			instance = new DateFilter(null);
		}
		return instance;
	}

	public DateFilter(String owner) {
		super("DateFilter", "Filters Dates", true, Optional.ofNullable(owner));
	}

	@Override
	protected List<FilterField<Long, ?>> getFilters() {
		return Arrays.asList(
			FilterField.isNull(),
			FilterField.create("equals", "Compares the date to the given ISO-8601 date for equality.", GraphQLString,
				dateTimePredicate(Instant::equals),
				Optional.of(query -> Comparison.eq(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(true, DateFilter::parseDate), query.getInitiatingFilterName()))),
			FilterField.create("notEquals", "Compares the date to the given ISO-8601 date for inequality.", GraphQLString,
					negate(dateTimePredicate(Instant::equals)),
					Optional.of(query -> Comparison.ne(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(true, DateFilter::parseDate), query.getInitiatingFilterName()))),
			FilterField.create("oneOf", "Tests if the date is equal to one of the given ISO-8601 dates.", GraphQLList.list(GraphQLString),
				this::oneOf, 
				Optional.of(query -> Comparison.in(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(true, DateFilter::parseDates), query.getInitiatingFilterName()))),
			FilterField.create("after", "Tests if the date is after the given ISO-8601 date.", GraphQLString, dateTimePredicate(Instant::isAfter),
				Optional.of(query -> Comparison.gt(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(true, DateFilter::parseDate), query.getInitiatingFilterName()))),
			FilterField.create("before", "Tests if the date is before the given ISO-8601 date.", GraphQLString, dateTimePredicate(Instant::isBefore),
				Optional.of(query -> Comparison.lt(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(true, DateFilter::parseDate), query.getInitiatingFilterName()))),
			FilterField.<Long, Boolean>create("isFuture", "Tests if the date is in the future.", GraphQLBoolean,
				query -> nullablePredicate(date -> Instant.ofEpochMilli(date).isAfter(Instant.now()) == query),
				Optional.of(query -> query.getQuery() 
						? Comparison.gt(query.makeFieldOperand(Optional.empty()), new LiteralOperand<>(Instant.now(), true), query.getInitiatingFilterName()) 
						: Comparison.lte(query.makeFieldOperand(Optional.empty()), new LiteralOperand<>(Instant.now(), true), query.getInitiatingFilterName()))),
			FilterField.<Long, Boolean>create("isPast", "Tests if the date is in the past.", GraphQLBoolean,
				query -> nullablePredicate(date -> Instant.ofEpochMilli(date).isBefore(Instant.now()) == query),
				Optional.of(query -> query.getQuery() 
						? Comparison.lt(query.makeFieldOperand(Optional.empty()), new LiteralOperand<>(Instant.now(), true), query.getInitiatingFilterName())
						: Comparison.gte(query.makeFieldOperand(Optional.empty()), new LiteralOperand<>(Instant.now(), true), query.getInitiatingFilterName()))));
	}

	private Function<String, Predicate<Long>> dateTimePredicate(BiPredicate<Instant, Instant> predicate) {
		return query -> {
			Instant queryDate = parseDate(query);
			return nullablePredicate(date -> predicate.test(parseLong(date), queryDate));
		};
	}

	private Function<String, Predicate<Long>> negate(Function<String, Predicate<Long>> predicate) {
		return query -> predicate.apply(query).negate();
	}

	private Predicate<Long> oneOf(List<String> query) {
		Set<Instant> dates = query.stream()
			.map(DateFilter::parseDate)
			.collect(Collectors.toSet());

		return nullablePredicate(date -> dates.contains(parseLong(date)));
	}

	private Instant parseLong(Long date) {
		return Instant.ofEpochMilli(date);
	}

	public static Collection<Instant> parseDates(Collection<String> dates) {
		return dates.stream().map(DateFilter::parseDate).collect(Collectors.toList());
	}

	public static Instant parseDate(String date) {
		// We allow different formats, that's why we go through a list of different parsers here
		List<Function<String, Instant>> parsers = Arrays.asList(
			d -> ZonedDateTime.parse(d).toInstant(),
			d -> LocalDateTime.parse(d).atZone(ZoneId.systemDefault()).toInstant(),
			d -> LocalDate.parse(d).atStartOfDay(ZoneId.systemDefault()).toInstant());

		DateTimeParseException lastException = null;
		for (Function<String, Instant> parser : parsers) {
			try {
				return parser.apply(date);
			} catch (DateTimeParseException e) {
				lastException = e;
			}
		}

		throw lastException;
	}
}
