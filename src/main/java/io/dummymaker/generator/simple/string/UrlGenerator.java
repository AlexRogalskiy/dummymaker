package io.dummymaker.generator.simple.string;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

import io.dummymaker.bundle.IBundle;
import io.dummymaker.bundle.impl.DomainBundle;
import io.dummymaker.bundle.impl.LoginBundle;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * Generates URL as string
 *
 * @author Anton Kurako (GoodforGod)
 * @since 4.5.2020
 */
public class UrlGenerator extends UriGenerator {

    private final Pattern pattern = Pattern.compile("site|website|url|server|link", CASE_INSENSITIVE);

    private static final IBundle domains = new LoginBundle();
    private static final IBundle zones = new DomainBundle();

    @Override
    public @NotNull String generate() {
        return "https://" + domains.random().replace(".", "") + zones.random() + super.generate();
    }

    @Override
    public @NotNull Pattern pattern() {
        return pattern;
    }
}
