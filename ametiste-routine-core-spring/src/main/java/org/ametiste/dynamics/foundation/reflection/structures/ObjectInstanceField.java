package org.ametiste.dynamics.foundation.reflection.structures;

import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.dynamics.foundation.elements.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * Represents a field element of a runtime objects graph, that belongs to an object.
 *
 * @apiNote This class is structural representation of the {@link Field} class in a context
 * of the Surface Model.
 *
 * @implNote Technically this object is an {@link Annotated} {@link Reference}, there is no any
 * special method for this class.
 *
 * @see ClassField
 * @see Reference
 * @see Annotated
 * @since 1.0
 */
@SurfaceStructure
public class ObjectInstanceField<T> implements Reference<T>, Annotated {

    private final ClassField classField;
    private final ObjectInstance<?> objectInstance;

    public ObjectInstanceField(final @NotNull ClassField classField,
                               final @NotNull ObjectInstance<?> objectInstance) {
        this.classField = classField;
        this.objectInstance = objectInstance;
    }

    /**
     * @since 1.0
     */
    @Override
    public boolean ofType(final @NotNull Class<?> type) {
        return type.isAssignableFrom(classField.type());
    }

    /**
     * @since 1.0
     */
    @NotNull
    @Override
    public Class<T> type() {
        return (Class<T>) classField.type();
    }

    /**
     * @implNote At the moment this method implement using the {@link ReflectionUtils} class.
     * @since 1.0
     */
    @Override
    public void referencesTo(final @NotNull T ref) {
        ReflectionUtils.makeAccessible(classField.field);
        ReflectionUtils.setField(classField.field, objectInstance.object, ref);
    }

    /**
     * @since 1.0
     */
    @NotNull
    @Override
    public <T extends AnnotationSpec> T annotation(final @NotNull Function<AnnotatedDescriptor, T> value) {
        return classField.annotation(value);
    }

    /**
     * @since 1.0
     */
    @Override
    public boolean hasAnnotations(final @NotNull Function<AnnotatedDescriptor, ? extends AnnotationSpec> handler) {
        return classField.hasAnnotations(handler);
    }

}