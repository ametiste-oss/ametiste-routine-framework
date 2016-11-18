package org.ametiste.dynamics.foundation.elements;

import org.ametiste.dynamics.RightSurface;
import org.ametiste.dynamics.Surface;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReflectionSurfaceBindingTest {

    private AnnotatedRefFeature<_TestType> refFeature;
    private _TestRefProcessor refProcessor;
    private RightSurface<AnnotatedRefFeature<_TestType>> surface;
    private ReflectionSurfaceBinding<_TestType, AnnotatedRefFeature<_TestType>, _TestContext> binding;
    private _TestContext context;

    @Before
    public void setUp() throws Exception {
        refFeature = mock(AnnotatedRefFeature.class);

        when(refFeature.<_TestAnnotationSpec>hasAnnotations(any())).thenReturn(true);
        when(refFeature.<_TestAnnotationSpec>annotation(any())).thenReturn(new _TestAnnotationSpec());

        refProcessor = new _TestRefProcessor();
        binding = new ReflectionSurfaceBinding<>(refProcessor);
        surface = Surface.rightSurface(refFeature);
        context = mock(_TestContext.class);
    }

    @Test
    public void adoptAnnotatedRefSurface() throws Exception {
        // doNothing().when(refProcessor).explode(eq(surface), eq(context));
        binding.explode(surface, context);
        refProcessor.verify(
                e -> e.isRefeneceTo(_TestType.class),
                c -> assertEquals(context, c)
        );
    }

}