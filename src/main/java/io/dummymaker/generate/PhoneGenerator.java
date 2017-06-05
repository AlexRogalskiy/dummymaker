package io.dummymaker.generate;


import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * Default Comment
 *
 * @author GoodforGod
 * @since 26.05.2017
 */
public class PhoneGenerator extends StringGenerator {

    @Override
    public String generate() {
        return current().nextInt(10)
                + "("
                + current().nextInt(100, 999)
                + ")"
                + current().nextInt(100, 999)
                + current().nextInt(1000, 9999);
    }
}
