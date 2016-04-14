package org.ametiste.routine;

/**
 * {@code RoutineCoreSpring} class describes module as object and contains its basic properties such as constants used
 * throughout the module.
 * <p>
 * Also class used to uniquely identify location of the module. See {@code AmetisteRoutineCoreConfiguration} for example
 * of module discovering usage.
 *
 * @see org.ametiste.routine.configuration.AmetisteRoutineCoreConfiguration
 */
public final class RoutineCoreSpring {

    public static final String PROPS_PREFIX = "org.ametiste.routine";
    public static final String MOD_PROPS_PREFIX = PROPS_PREFIX + ".mod";
    public static final String CORE_PROPS_PREFIX = PROPS_PREFIX + ".core";

    public static final String ROUTINE_CORE_PROPERTIES = "classpath:/routine.properties";

}
