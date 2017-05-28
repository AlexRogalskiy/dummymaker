package io.generator.stock;

import io.generator.IGenerator;

import java.util.UUID;

/**
 * Default Comment
 *
 * @author @GoodforGod
 * @since 26.05.2017
 */
public class StringGenerator implements IGenerator<String> {

    @Override
    public String generate() {
        return UUID.randomUUID().toString().replace('-', ' ');
    }
}
