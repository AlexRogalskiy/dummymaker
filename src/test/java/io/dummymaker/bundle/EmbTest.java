package io.dummymaker.bundle;

import io.dummymaker.data.DummyEmbedded;
import io.dummymaker.factory.IProduceFactory;
import io.dummymaker.factory.impl.AutoGeneratorsFactory;
import io.dummymaker.factory.impl.GenProduceFactory;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * "default comment"
 *
 * @author GoodforGod
 * @since 23.04.2018
 */
public class EmbTest {

    @Test
    public void test() {
        IProduceFactory factory = new GenProduceFactory();
        AutoGeneratorsFactory factory1 = new AutoGeneratorsFactory();

        DummyEmbedded emb = factory.produce(DummyEmbedded.class);
        factory1.availableGenContainers();
        assertNotNull(emb);
    }
}
