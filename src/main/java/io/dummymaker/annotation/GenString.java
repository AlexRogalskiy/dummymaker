package io.dummymaker.annotation;


import io.dummymaker.annotation.prime.PrimeGenAnnotation;
import io.dummymaker.generate.StringGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Default Comment
 *
 * @author GoodforGod
 * @since 30.05.2017
 */
@PrimeGenAnnotation(StringGenerator.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GenString {

}
