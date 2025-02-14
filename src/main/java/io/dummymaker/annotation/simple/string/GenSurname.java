package io.dummymaker.annotation.simple.string;

import io.dummymaker.annotation.core.PrimeGen;
import io.dummymaker.generator.simple.string.SurnameGenerator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 06.03.2019
 */
@PrimeGen(SurnameGenerator.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GenSurname {

}
