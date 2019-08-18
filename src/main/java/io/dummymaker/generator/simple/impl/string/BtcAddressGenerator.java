package io.dummymaker.generator.simple.impl.string;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Generates bitcoin address
 *
 * @author GoodforGod
 * @since 04.11.2018
 */
public class BtcAddressGenerator extends IdGenerator {

    private final Pattern pattern = Pattern.compile("btc|bitcoin", CASE_INSENSITIVE);

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String generate() {
        return super.generate().replace("-", "") + super.generate().substring(0, 2).replace("-", "");
    }
}
