package org.ametiste.dynamics.foundation.reflection.structures;

import org.ametiste.dynamics.foundation.MockConsumer;
import org.ametiste.dynamics.foundation.MockFuncs;
import org.ametiste.dynamics.foundation.MockFunction;
import org.ametiste.dynamics.foundation.reflection.DynamicsRuntime;
import org.ametiste.dynamics.foundation.reflection.RuntimePattern;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static org.mockito.Mockito.*;

/**
 *
 * @since
 */
public class ClassPoolTest {

    @Mock
    private DynamicsRuntime preSurface;
    private ClassPool classPool;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        classPool = new ClassPool(preSurface);
    }

    @Test
    public void testClassStructuresMatchingWithMapper() throws Exception {

        final Object mappedObject = new Object();
        final UnaryOperator<RuntimePattern> pattern = p -> RuntimePattern.create(pp -> RuntimePattern.EMPTY);
        final Consumer<Class<?>> consumer = o -> {};

        final MockConsumer<Object> mockConsumer = MockFuncs
                .consumer(a -> a.equals(mappedObject));

        final MockFunction<ClassStructure<Object>, Object> mockMapper = MockFuncs
                .function(a -> a.type().equals(Object.class), mappedObject);

        doAnswer(a -> {
            ((Consumer<Class<?>>)(a.getArguments()[1])).accept(Object.class);
            return null;
        }).when(preSurface).findClasses(eq(pattern), any());

        classPool.classes(
            pattern,
            mockMapper::apply,
            mockConsumer::accept
        );

        verify(preSurface).findClasses(eq(pattern), any());
        mockConsumer.verify();
        mockMapper.verify();
    }

    @Test
    public void testClassStructuresMatching() throws Exception {

        final UnaryOperator<RuntimePattern> pattern = p -> RuntimePattern.create(pp -> RuntimePattern.EMPTY);
        final Consumer<Class<?>> consumer = o -> {};

        final MockConsumer<ClassStructure> mockConsumer = MockFuncs
                .consumer(a -> a.type().equals(Object.class));

        doAnswer(a -> {
            ((Consumer<Class<?>>)(a.getArguments()[1])).accept(Object.class);
            return null;
        }).when(preSurface).findClasses(eq(pattern), any());

        classPool.classes(
            pattern,
            mockConsumer::accept
        );

        verify(preSurface).findClasses(eq(pattern), any());
        mockConsumer.verify();
    }

}