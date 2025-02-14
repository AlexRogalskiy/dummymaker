package io.dummymaker.generator.simple.string;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

import io.dummymaker.bundle.IBundle;
import io.dummymaker.bundle.impl.JobBundle;
import io.dummymaker.generator.IGenerator;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * Job name generator
 *
 * @author GoodforGod
 * @since 16.07.2019
 */
public class JobGenerator implements IGenerator<String> {

    private final Pattern pattern = Pattern.compile("qualification|job|work|position", CASE_INSENSITIVE);

    private static final IBundle bundle = new JobBundle();

    @Override
    public @NotNull String generate() {
        return bundle.random();
    }

    @Override
    public @NotNull Pattern pattern() {
        return pattern;
    }
}
