package org.ametiste.dynamics.foundation;

import org.ametiste.dynamics.DynamicSurfaceStructure;
import org.ametiste.dynamics.Surface;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GeneralDelegate implements Surface {

    private final String name;
    private final Map<Class<?>, Object> features;


    public GeneralDelegate(final String name,
                           final Object ... features) {
        this(name, Arrays.asList(features));
    }

    public GeneralDelegate(final String name,
                           final List<Object> features) {
        this.name = name;
        this.features = features.stream().collect(
                Collectors.toMap(k -> k.getClass(), v -> v)
        );
    }

    public <T> T mapName(final Function<String, T> transform) {
        return transform.apply(name);
    }

    public boolean actsLike(final DynamicSurfaceStructure dynamicSurfaceStructure) {
        return dynamicSurfaceStructure.test(this);
    }

    @Override
    public <F, T> T map(final Function<F, T> transformFeature, final Class<F> feature) {
        return transformFeature.apply(feature.cast(features.get(feature)));
    }

    @Override
    public <F> boolean satisfiedBy(final Predicate<F> predicate, final Class<F> feature) {
        return predicate.test(feature.cast(features.get(feature)));
    }
}