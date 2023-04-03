package com.gentics.graphqlfilter.filter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.gentics.graphqlfilter.model.Node;

public class NodeFilter extends MainFilter<Node> {

	private static NodeFilter instance;

	public static NodeFilter filter() {
		if (instance == null) {
			instance = new NodeFilter();
		}
		return instance;
	}

	private NodeFilter() {
		super("NodeFilter", "Filters Nodes", false, Optional.empty());
	}

	@Override
	protected List<FilterField<Node, ?>> getFilters() {
		return Arrays.asList(
			new MappedFilter<>("NODE", "uuid", "Filters by uuid", StringFilter.filter(), Node::getUuid),
			new MappedFilter<>("NODE", "schema", "Filters by Schema", SchemaFilter.filter(), Node::getSchema),
			new MappedFilter<>("NODE", "language", "Filters by Language", StringFilter.filter(), Node::getLanguage),
			new MappedFilter<>("NODE", "name", "Filters by name", StringFilter.filter(), Node::getName),
			new MappedFilter<>("NODE", "published", "Filters by published state", BooleanFilter.filter(), Node::isPublished),
			new MappedFilter<>("NODE", "created", "Filters by creation date", DateFilter.filter(), Node::getCreated),
			new MappedFilter<>("NODE", "price", "Filters by price", NumberFilter.filter(),
				node -> node.getPrice() == null ? null : new BigDecimal(node.getPrice().toString())));
	}
}
