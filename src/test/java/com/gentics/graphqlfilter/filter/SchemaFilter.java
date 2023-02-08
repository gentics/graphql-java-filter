package com.gentics.graphqlfilter.filter;

import static com.gentics.graphqlfilter.util.FilterUtil.nullablePredicate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.gentics.graphqlfilter.filter.operation.Comparison;
import com.gentics.graphqlfilter.model.Schema;

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
		super("SchemaFilter", "Filters schemas", false, Optional.empty());
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
			new MappedFilter<>("SCHEMA", "name", "Filters by name", StringFilter.filter(), Schema::getName),
			new MappedFilter<>("SCHEMA", "uuid", "Filters by uuid", StringFilter.filter(), Schema::getUuid),
			FilterField.<Schema, String>create("is", "Filters by Schema Type", getSchemaEnum(),
				name -> nullablePredicate(schema -> schema.getName().equals(name)), 
				Optional.of(query -> Comparison.eq(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(true)))));
	}
}
