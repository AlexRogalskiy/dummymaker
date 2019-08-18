package io.dummymaker.beta;

import io.dummymaker.beta.model.DummyEmbedded;
import io.dummymaker.data.DummyAuto;
import io.dummymaker.factory.impl.GenFactory;
import io.dummymaker.generator.simple.impl.number.ByteGenerator;
import io.dummymaker.generator.simple.impl.number.ShortGenerator;
import io.dummymaker.model.GenRule;
import io.dummymaker.model.GenRules;
import org.junit.Test;

/**
 * "default comment"
 *
 * @author GoodforGod
 * @since 01.08.2019
 */
public class Tester {

    @Test
    public void test() {
        GenRules rules = GenRules.of(
                GenRule.of(DummyAuto.class)
                        .add(ByteGenerator.class, "aLong")
                        .add(ShortGenerator.class, int.class)
        );


    }

    @Test
    public void testEmbedded() {
        GenFactory factory = new GenFactory();
        DummyEmbedded build = factory.build(DummyEmbedded.class);
        System.out.println(build);
        System.out.println(build.getChild());
        System.out.println(build.getSimpleChild());
    }
}
