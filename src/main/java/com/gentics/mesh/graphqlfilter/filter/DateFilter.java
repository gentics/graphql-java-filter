package com.gentics.mesh.graphqlfilter.filter;

import graphql.schema.GraphQLList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static graphql.Scalars.GraphQLBoolean;
import static graphql.Scalars.GraphQLString;

/**
 * Filters ISO-8601 strings by various means
 */
public class DateFilter extends MainFilter<String> {

    private static DateFilter instance;

    /**
     * Filters ISO-8601 strings by various means
     */
    public static DateFilter filter() {
        if (instance == null) {
            instance = new DateFilter();
        }
        return instance;
    }

    private DateFilter() {
        super("DateFilter", "Filters Dates");
    }

    @Override
    protected List<FilterField<String, ?>> getFilters() {
        return Arrays.asList(
            FilterField.create("equals", "Compares two dates for equality.", GraphQLString, dateTimePredicate(ZonedDateTime::equals)),
            FilterField.create("oneOf", "Tests if the date is equal to one of the given dates", GraphQLList.list(GraphQLString), this::oneOf),
            FilterField.create("after", "Tests if the date is after the given date.", GraphQLString, dateTimePredicate(ZonedDateTime::isAfter)),
            FilterField.create("before", "Tests if the date is before the given date.", GraphQLString, dateTimePredicate(ZonedDateTime::isBefore)),
            FilterField.create("isFuture", "Tests if the date is in the future.", GraphQLBoolean, query -> date -> parseDate(date).isAfter(ZonedDateTime.now()) == (boolean) query),
            FilterField.create("isPast", "Tests if the date is in the past.", GraphQLBoolean, query -> date -> parseDate(date).isBefore(ZonedDateTime.now()) == (boolean) query)
        );
    }

    private Function<String, Predicate<String>> dateTimePredicate(BiPredicate<ZonedDateTime, ZonedDateTime> predicate) {
        return query -> {
            ZonedDateTime queryDate = parseDate(query);
            return date -> predicate.test(parseDate(date), queryDate);
        };
    }

    private Predicate<String> oneOf(List<String> query) {
        Set<ZonedDateTime> dates = query.stream()
            .map(this::parseDate)
            .collect(Collectors.toSet());

        return date -> dates.contains(parseDate(date));
    }

    private ZonedDateTime parseDate(String date) {
        // We allow different formats, that's why we go through a list of different parsers here
        List<Function<String, ZonedDateTime>> parsers = Arrays.asList(
            ZonedDateTime::parse,
            d -> LocalDateTime.parse(d).atZone(ZoneId.systemDefault()),
            d -> LocalDate.parse(d).atStartOfDay(ZoneId.systemDefault())
        );

        DateTimeParseException lastException = null;
        for (Function<String, ZonedDateTime> parser : parsers) {
            try {
                return parser.apply(date);
            } catch (DateTimeParseException e) {
                lastException = e;
            }
        }

        throw lastException;
    }
}
