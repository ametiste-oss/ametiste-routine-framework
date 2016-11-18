package org.ametiste.dynamics.foundation.elements;

import org.ametiste.dynamics.RightSurface;
import org.ametiste.dynamics.Surface;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.context.TestContext;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AnnotatedRefProcessorTest {

    private AnnotatedRefFeature<_TestType> refFeature;
    private AnnotatedRefProcessor<_TestAnnotationSpec, _TestType, TestContext> refProcessor;
    private RightSurface<AnnotatedRefFeature<_TestType>> surface;
    private TestContext context;

    @Before
    public void setUp() throws Exception {
        refFeature = mock(AnnotatedRefFeature.class);
        refProcessor = mock(AnnotatedRefProcessor.class, Mockito.CALLS_REAL_METHODS);

        surface = Surface.rightSurface(refFeature);
        context = mock(TestContext.class);

        when(refFeature.ofType(_TestType.class)).thenReturn(true);
        when(refFeature.type()).thenReturn(_TestType.class);

    }

    @Test
    public void elementHasAnnotations() throws Exception {

        when(refFeature.hasAnnotations(any())).thenReturn(true);
        when(refFeature.annotation(any())).thenReturn(new _TestAnnotationSpec());

        refProcessor.explode(surface, context);

        verify(refProcessor).resolveValue(any(), any());
    }

    @Test
    public void elementHasNoAnnotations() throws Exception {

        when(refFeature.hasAnnotations(any())).thenReturn(false);

        refProcessor.explode(surface, context);

        verify(refProcessor, times(0)).resolveValue(any(), any());
    }


}