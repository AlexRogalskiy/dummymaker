package io.dummymaker.factory;

import io.dummymaker.bundle.impl.FemaleNameBundle;
import io.dummymaker.bundle.impl.MaleNameBundle;
import io.dummymaker.bundle.impl.NounBundle;
import io.dummymaker.factory.impl.GenFactory;
import io.dummymaker.generator.simple.number.ByteGenerator;
import io.dummymaker.generator.simple.string.NounGenerator;
import io.dummymaker.model.DummyEmbedded;
import io.dummymaker.model.GenRule;
import io.dummymaker.model.GenRules;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;

/**
 * Global Gen rules tests
 *
 * @author GoodforGod
 * @since 15.10.2019
 */
public class DummyGlobalRulesTests extends Assert {

    private final GenFactory factory = new GenFactory(GenRules.of(
            GenRule.global(2)
                    .add(ByteGenerator.class, int.class)
                    .add(NounGenerator.class, "name")));

    @Test
    public void validFieldsGeneration() {
        final DummyEmbedded dummy = factory.build(DummyEmbedded.class);

        final Set<String> nouns = Arrays.stream(new NounBundle().all()).collect(Collectors.toSet());
        final Set<String> names = Stream.of(new MaleNameBundle().all(), new FemaleNameBundle().all())
                .flatMap(Arrays::stream)
                .collect(Collectors.toSet());

        assertNotNull(dummy);
        assertNotNull(dummy.getId());
        assertNotNull(dummy.getChild());
        assertNotNull(dummy.getSimpleChild());
        assertNotNull(dummy.getName());
        assertTrue(nouns.contains(dummy.getName()));

        assertTrue(dummy.getSimpleChild().getNumber() < Byte.MAX_VALUE);
        assertNotNull(dummy.getSimpleChild().getSimpleName());
        assertTrue(names.contains(dummy.getSimpleChild().getSimpleName()));

        assertNotNull(dummy.getChild().getName());
        assertTrue(nouns.contains(dummy.getChild().getName()));
    }
}
