package io.dummymaker.factory.impl;

import io.dummymaker.annotation.special.GenSequence;
import io.dummymaker.factory.IGenStorage;
import io.dummymaker.factory.IGenSupplier;
import io.dummymaker.generator.simple.IGenerator;
import io.dummymaker.generator.simple.impl.NullGenerator;
import io.dummymaker.generator.simple.impl.SequenceGenerator;
import io.dummymaker.model.GenContainer;
import io.dummymaker.model.GenRules;
import io.dummymaker.model.graph.Node;
import io.dummymaker.model.graph.Payload;
import io.dummymaker.scan.IPopulateScanner;
import io.dummymaker.scan.impl.SequenceScanner;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.dummymaker.util.CastUtils.instantiate;

/**
 * Storage that facilitates generator storage, scanners, field mapping and nullable fields
 * To improve factory performance and extend complex generators abilities
 *
 * @author GoodforGod
 * @since 17.07.2019
 */
class GenStorage implements IGenStorage {

    private final IGenSupplier supplier;
    private final GenFactory embeddedFactory; // stupid? yes, have better solution pls PR
    private final IPopulateScanner scanner;
    private final GenGraphBuilder graphBuilder;
    private final GenRules rules;

    private final Map<Class<? extends IGenerator>, IGenerator> generators;
    private final Map<Class<?>, Map<Field, IGenerator>> sequentialGenerators;
    private final Map<Class<?>, Map<Field, GenContainer>> containers;
    private final Set<Field> marked;

    private Node<Payload> graph;

    GenStorage(IPopulateScanner scanner, GenRules rules) {
        this.scanner = scanner;
        this.rules = rules;

        this.embeddedFactory = new GenFactory(scanner);
        this.graphBuilder = new GenGraphBuilder(scanner);
        this.supplier = new GenSupplier();

        this.sequentialGenerators = new HashMap<>();
        this.containers = new HashMap<>();
        this.generators = new HashMap<>();
        this.marked = new HashSet<>();
    }

    @Override
    public IGenerator getGenerator(Class<? extends IGenerator> generatorClass) {
        return (generatorClass == null)
                ? generators.computeIfAbsent(NullGenerator.class, (k) -> instantiate(NullGenerator.class))
                : generators.computeIfAbsent(generatorClass, (k) -> instantiate(generatorClass));
    }

    @Override
    public Class<? extends IGenerator> getSuitable(Field field) {
        return supplier.getSuitable(field);
    }

    @Override
    public Class<? extends IGenerator> getSuitable(Field field, Class<?> type) {
        return supplier.getSuitable(field, type);
    }

    /**
     * @param t     entity
     * @param depth to start entity data fill with
     * @param <T>   type
     * @return entity filled with data
     */
    @Override
    public <T> T fillByDepth(T t, int depth) {
        return embeddedFactory.fillEntity(t, this, depth);
    }

    /**
     * Full scanned containers to field map
     *
     * @return gen container map to field
     */
    Map<Field, GenContainer> getContainers(Object t) {
        if (t == null)
            return Collections.emptyMap();

        final Class<?> target = t.getClass();

        // when encounters first object generation (always top level object)
        if (this.graph == null)
            this.graph = graphBuilder.build(target);

        markSequentialFields(target);

        final boolean parentMarked = isAnyParentMarked(target);
        return containers.computeIfAbsent(target, k -> scanner.scan(target, parentMarked));
    }

    /**
     * Checks whenever any parent is marked as gen auto
     *
     * @param target to check
     * @return true if any parent marked
     */
    private boolean isAnyParentMarked(Class<?> target) {
        final Predicate<Node<Payload>> filter = n -> n.value().getType().equals(target);
        if(filter.test(graph))
            return true;

        final Optional<Node<Payload>> node = graphBuilder.find(graph, filter);
        return node.filter(payloadNode -> haveMarkedParent(payloadNode, 1)).isPresent();

    }

    /**
     * @param node  to scan
     * @param depth level to validate
     * @return true if parent or start node is marked
     * @see #isAnyParentMarked(Class)
     */
    private boolean haveMarkedParent(Node<Payload> node, int depth) {
        if (node.value().isMarkedAuto())
            return node.value().getDepth() >= depth;

        return node.getParent() != null && haveMarkedParent(node.getParent(), depth + 1);
    }

    int getDepth(Class<?> parent, Class<?> target) {
        final Predicate<Node<Payload>> filter = n -> n.getParent() != null
                && n.getParent().value().getType().equals(parent)
                && n.value().getType().equals(target);

        return graphBuilder.find(graph, filter)
                .map(n -> n.getParent().value().getDepth())
                .orElse(1);
    }

    /**
     * Was field marked with sequence generator
     *
     * @param target class
     * @param field  to check
     * @return is marked sequential
     */
    boolean isSequential(Class<?> target, Field field) {
        return sequentialGenerators.getOrDefault(target, Collections.emptyMap()).containsKey(field);
    }

    /**
     * Gets sequence generator for class field
     *
     * @param target class
     * @param field  to check
     * @return sequence generator
     */
    IGenerator getSequential(Class<?> target, Field field) {
        return sequentialGenerators.computeIfAbsent(target, (k) -> {
            final Map<Field, IGenerator> map = new HashMap<>();
            map.put(field, new SequenceGenerator());
            return map;
        }).get(field);
    }

    /**
     * Was field marked as nullable/invalid/etc
     *
     * @param field to check
     * @return was not marked or otherwise
     */
    boolean isUnmarked(Field field) {
        return !marked.contains(field);
    }

    /**
     * Mark field as nullable
     *
     * @param field to mark
     */
    void markNullable(Field field) {
        marked.add(field);
    }

    /**
     * Scan for sequence annotation marked fields
     *
     * @param target class to scan
     */
    private void markSequentialFields(Class<?> target) {
        this.sequentialGenerators.computeIfAbsent(target, (k) -> new SequenceScanner().scan(target)
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new SequenceGenerator(((GenSequence) e.getValue().get(0)).from()))
                ));

    }
}
