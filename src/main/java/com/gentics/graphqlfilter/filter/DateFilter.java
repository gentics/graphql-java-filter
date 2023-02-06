package com.gentics.graphqlfilter.filter;

import graphql.schema.GraphQLList;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.gentics.graphqlfilter.filter.sql.ComparisonPredicate;

import static com.gentics.graphqlfilter.util.FilterUtil.nullablePredicate;
import static graphql.Scalars.GraphQLBoolean;
import static graphql.Scalars.GraphQLString;

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
			instance = new DateFilter();
		}
		return instance;
	}

	private DateFilter() {
		super("DateFilter", "Filters Dates");
	}

	@Override
	protected List<FilterField<Long, ?>> getFilters() {
		return Arrays.asList(
			FilterField.isNull(),
			FilterField.create("equals", "Compares the date to the given ISO-8601 date for equality.", GraphQLString,
				dateTimePredicate(Instant::equals),
				Optional.of((field, compared) -> new ComparisonPredicate<>("=", field, compared, true))),
			FilterField.create("oneOf", "Tests if the date is equal to one of the given ISO-8601 dates.", GraphQLList.list(GraphQLString),
				this::oneOf, Optional.of((field, compared) -> new ComparisonPredicate<>("=", field, compared, true))),
			FilterField.create("after", "Tests if the date is after the given ISO-8601 date.", GraphQLString, dateTimePredicate(Instant::isAfter),
				Optional.of((field, compared) -> new ComparisonPredicate<>(">", field, compared, true))),
			FilterField.create("before", "Tests if the date is before the given ISO-8601 date.", GraphQLString, dateTimePredicate(Instant::isBefore),
				Optional.of((field, compared) -> new ComparisonPredicate<>("<", field, compared, true))),
			FilterField.<Long, Boolean>create("isFuture", "Tests if the date is in the future.", GraphQLBoolean,
				query -> nullablePredicate(date -> Instant.ofEpochMilli(date).isAfter(Instant.now()) == query),
				Optional.of((field, compared) -> new ComparisonPredicate<>(">", field, Instant.now(), true))),
			FilterField.<Long, Boolean>create("isPast", "Tests if the date is in the past.", GraphQLBoolean,
				query -> nullablePredicate(date -> Instant.ofEpochMilli(date).isBefore(Instant.now()) == query),
				Optional.of((field, compared) -> new ComparisonPredicate<>("<", field, Instant.now(), true))));
	}

	private Function<String, Predicate<Long>> dateTimePredicate(BiPredicate<Instant, Instant> predicate) {
		return query -> {
			Instant queryDate = parseDate(query);
			return nullablePredicate(date -> predicate.test(parseLong(date), queryDate));
		};
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
