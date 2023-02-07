package com.gentics.graphqlfilter.filter;

import static com.gentics.graphqlfilter.util.FilterUtil.nullablePredicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.gentics.graphqlfilter.filter.sql.ComparisonPredicate;
import com.gentics.graphqlfilter.model.Schema;
import com.gentics.graphqlfilter.util.FilterUtil;

import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLInputType;

public class SchemaFilter extends MainFilter<Schema> {

	private static SchemaFilter instance;

	public static SchemaFilter filter() {
		if (instance == null) {
			instance = new SchemaFilter();
		}
		return instance;
	}

	private SchemaFilter() {
		super("SchemaFilter", "Filters schemas", false);
	}

	private static GraphQLInputType getSchemaEnum() {
		return GraphQLEnumType.newEnum()
			.name("SchemaEnum")
			.value("folder", "folder")
			.value("content", "content")
			.build();
	}

	@Override
	protected List<FilterField<Schema, ?>> getFilters() {
		return Arrays.asList(
			new MappedFilter<>("name", "Filters by name", StringFilter.filter(), Schema::getName),
			new MappedFilter<>("uuid", "Filters by uuid", StringFilter.filter(), Schema::getUuid),
			FilterField.<Schema, String>create("is", "Filters by Schema Type", getSchemaEnum(),
				name -> nullablePredicate(schema -> schema.getName().equals(name)), 
				Optional.of((query, fields) -> new ComparisonPredicate<>("=", FilterUtil.addFluent(new ArrayList<>(fields), "name"), query, true))));
	}
}
