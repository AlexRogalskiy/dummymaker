package io.dummymaker.annotation.simple.number;

import io.dummymaker.annotation.core.PrimeGen;
import io.dummymaker.generator.simple.number.CharacterGenerator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author GoodforGod
 * @see CharacterGenerator
 * @since 04.11.2018
 */
@PrimeGen(CharacterGenerator.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GenCharacter {

}
