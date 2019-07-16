package io.dummymaker.generator.simple.impl.string;

import io.dummymaker.bundle.IBundle;
import io.dummymaker.bundle.impl.NounBundle;
import io.dummymaker.generator.simple.IGenerator;

/**
 * "default comment"
 *
 * @author GoodforGod
 * @since 21.02.2018
 */
public class NounGenerator implements IGenerator<String> {

    private final IBundle<String> bundle = new NounBundle();

    @Override
    public String generate() {
        return bundle.getRandom();
    }
}
