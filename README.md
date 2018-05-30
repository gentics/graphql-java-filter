# GraphQL Filter
Enhance your GraphQL API with filters.

Use this library to let the user filter through the results of a GraphQL query.

## Live Demo
[Here is an implementation of this library.](https://demo.getmesh.io/api/v1/demo/graphql/browser)

Try this query:

```graphql
{
  nodes(filter: {
    schema: { is: vehicle },
    fields: {
      vehicle: {
        price: {
          lt: 2000000
        }
      }
    }
  }) {
    elements {
      fields {
        ... on vehicle {
          name
          price
        }
      }
    }
  }
}
```

Play around with the autocompletion to see whats possible.

## Usage
Create your filter by implementing the `Filter` interface or by extending one of the predefined abstract filters. It is best to create your filter by composing the small predefined filters.

See [AbstractFilterTest.java](src/test/java/com/gentics/graphqlfilter/AbstractFilterTest.java) to see an example of how to integrate this library in your environment.

## Overview

### Interfaces
* `Filter` The main Filter interface. Every filter implements this.
* `FilterField` can be used to create filters that can easily be nested in other filters.
* `StartFilter` is used to easily create the argument object for `graphql-java`

### Helper filters
* `MainFilter` A filter that does not filter directly and instead contains a collection of other filters.
* `StartMainFilter` Same as above but additionally implements StartFilter.
* `MappedFilter` Used to map a type into another type using another Filter
* `CommonFilters` Provides common filters which can be used for all types. This includes these logical operations of filters: `and`, `or` and `not`.

### Predefined filters
The following filters can be used to filter primitive types: `BooleanFilter`, `DateFilter`, `NumberFilter`, `StringFilter`.

## Examples
See [NodeFilter.java](src/test/java/com/gentics/graphqlfilter/filter/NodeFilter.java)  as an example filter implementation.