package io.dummymaker.generator.simple.string;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

import io.dummymaker.generator.IGenerator;
import io.dummymaker.generator.simple.number.LongGenerator;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * Generates number represented as hex string
 *
 * @author GoodforGod
 * @since 04.11.2018
 */
public class HexNumberGenerator implements IGenerator<String> {

    private final Pattern pattern = Pattern.compile("hex(num(ber)?)?", CASE_INSENSITIVE);

    @Override
    public @NotNull String generate() {
        return Long.toHexString(new LongGenerator().generate());
    }

    @Override
    public @NotNull Pattern pattern() {
        return pattern;
    }
}
