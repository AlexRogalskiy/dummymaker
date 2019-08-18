package io.dummymaker.scan;

import io.dummymaker.model.GenContainer;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Scanners used by populate factory primarily
 *
 * @author GoodforGod
 * @see io.dummymaker.factory.impl.GenFactory
 * @see GenContainer
 * @since 10.03.2018
 */
public interface IPopulateScanner extends IMapScanner<Field, GenContainer, Class> {

    /**
     * Scan for annotated target fields
     *
     * @param target to scan
     * @return map of fields and its gen containers
     */
    Map<Field, GenContainer> scan(Class target);
}
