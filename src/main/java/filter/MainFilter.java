//package filter;
//
//import graphql.schema.GraphQLInputObjectField;
//import graphql.schema.GraphQLInputType;
//import graphql.schema.GraphQLList;
//import graphql.schema.GraphQLTypeReference;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;
//
//import java.security.InvalidParameterException;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static graphql.schema.GraphQLInputObjectType.newInputObject;
//
//final public class MainFilter<T> implements Filter<T> {
//
//    //    public static final List<Filter<Node>> STATIC_NODE_FILTERS = Arrays.asList(
////    );
//    private final String name;
//    private final String description;
//    private final Map<String, Filter<T>> filters;
//
//    public MainFilter(String name, String description, List<Filter<T>> filters) {
//        this.name = name;
//        this.description = description;
//        this.filters = Stream.concat(
//            filters.stream(),
//            commonFilters()
//        ).collect(Collectors.toMap(Filter::getName, Function.identity()));
//    }
//
//    @Override
//    public GraphQLInputType getType() {
//        return newInputObject()
//            .name(getName())
//            .description(getDescription())
//            .fields(toObjectFields())
//            .build();
//    }
//
//    private List<GraphQLInputObjectField> toObjectFields() {
//        return filters.values().stream()
//            .map(Filter::toObjectField)
//            .collect(Collectors.toList());
//    }
//
//    @Override
//    public String getName() {
//        return name;
//    }
//
//    @Override
//    public String getDescription() {
//        return description;
//    }
//
//    @Override
//    public Predicate<T> createPredicate(Object query) {
//        if (!(query instanceof Map)) {
//            throw new NotImplementedException();
//        }
//        return ((Map<String, Object>)query).entrySet().stream()
//            .map(entry -> filters.get(entry.getKey()).createPredicate(entry.getValue()))
//            .reduce(Predicate::and)
//            .orElse(ignore -> true);
//    }
//
//    private Filter<T> orFilter() {
//        return new Filter<T>() {
//            @Override
//            public String getName() {
//                return "or";
//            }
//
//            @Override
//            public String getDescription() {
//                return "Combines multiple " + MainFilter.this.getName() + " with logical OR";
//            }
//
//            @Override
//            public Predicate<T> createPredicate(Object query) {
//                if (!(query instanceof List)) {
//                    throw new InvalidParameterException();
//                }
//                return ((List<Map<String, Object>>)query).stream()
//                    .map(MainFilter.this::createPredicate)
//                    .reduce((p, q) -> p.or(q))
//                    .orElse(ignore -> true);
//            }
//
//            @Override
//            public GraphQLInputType getType() {
//                return GraphQLList.list(GraphQLTypeReference.typeRef(MainFilter.this.getName()));
//            }
//        };
//    }
//
//    private Filter<T> andFilter() {
//        return new Filter<T>() {
//            @Override
//            public String getName() {
//                return "and";
//            }
//
//            @Override
//            public String getDescription() {
//                return "Combines multiple " + MainFilter.this.getName() + " with logical AND";
//            }
//
//            @Override
//            public Predicate<T> createPredicate(Object query) {
//                if (!(query instanceof List)) {
//                    throw new InvalidParameterException();
//                }
//                return ((List<Map<String, Object>>)query).stream()
//                    .map(MainFilter.this::createPredicate)
//                    .reduce((p, q) -> p.and(q))
//                    .orElse(ignore -> true);
//            }
//
//            @Override
//            public GraphQLInputType getType() {
//                return GraphQLList.list(GraphQLTypeReference.typeRef(MainFilter.this.getName()));
//            }
//        };
//    }
//
//    private Filter<T> notFilter() {
//        return new Filter<T>() {
//            @Override
//            public String getName() {
//                return "not";
//            }
//
//            @Override
//            public String getDescription() {
//                return "Negates a " + MainFilter.this.getName();
//            }
//
//            @Override
//            public Predicate<T> createPredicate(Object query) {
//                return MainFilter.this.createPredicate(query).negate();
//            }
//
//            @Override
//            public GraphQLInputType getType() {
//                return GraphQLTypeReference.typeRef(MainFilter.this.getName());
//            }
//        };
//    }
//
//    private Stream<Filter<T>> commonFilters() {
//        return Stream.of(orFilter(), andFilter(), notFilter());
//    }
//}
