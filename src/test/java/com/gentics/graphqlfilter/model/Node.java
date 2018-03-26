package com.gentics.graphqlfilter.model;

public class Node {

	private final String uuid;
	private final Schema schema;
	private final Long created;
	private final String language;
	private final String name;
	private final Boolean published;
	private final Double price;

	public Node(String uuid, Schema schema, Long created, String language, String name, Boolean published, Double price) {
		this.uuid = uuid;
		this.schema = schema;
		this.created = created;
		this.language = language;
		this.name = name;
		this.published = published;
		this.price = price;
	}

	public Long getCreated() {
		return created;
	}

	public String getLanguage() {
		return language;
	}

	public String getUuid() {
		return uuid;
	}

	public Schema getSchema() {
		return schema;
	}

	public String getName() {
		return name;
	}

	public Boolean isPublished() {
		return published;
	}

	public Double getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "Node{" +
			"uuid='" + uuid + '\'' +
			", schema=" + schema +
			", created=" + created +
			", language='" + language + '\'' +
			", name='" + name + '\'' +
			", published=" + published +
			", price=" + price +
			'}';
	}
}
