package io.dummymaker.annotation.simple.string;

import io.dummymaker.annotation.core.PrimeGen;
import io.dummymaker.generator.simple.string.FrequencyGenerator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Anton Kurako (GoodforGod)
 * @see FrequencyGenerator
 * @since 26.08.2022
 */
@PrimeGen(FrequencyGenerator.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GenFrequency {

}
