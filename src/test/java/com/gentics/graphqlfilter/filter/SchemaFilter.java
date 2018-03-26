package com.gentics.graphqlfilter.filter;

import com.gentics.graphqlfilter.filter.FilterField;
import com.gentics.graphqlfilter.filter.MainFilter;
import com.gentics.graphqlfilter.filter.MappedFilter;
import com.gentics.graphqlfilter.filter.StringFilter;
import com.gentics.graphqlfilter.model.Schema;

import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLInputType;

import static com.gentics.graphqlfilter.util.FilterUtil.nullablePredicate;

import java.util.Arrays;
import java.util.List;

public class SchemaFilter extends MainFilter<Schema> {

	private static SchemaFilter instance;

	public static SchemaFilter filter() {
		if (instance == null) {
			instance = new SchemaFilter();
		}
		return instance;
	}

	private SchemaFilter() {
		super("SchemaFilter", "Filters schemas");
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
				name -> nullablePredicate(schema -> schema.getName().equals(name))));
	}
}
