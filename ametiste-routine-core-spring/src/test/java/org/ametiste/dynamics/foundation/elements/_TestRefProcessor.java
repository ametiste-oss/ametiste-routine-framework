package org.ametiste.dynamics.foundation.elements;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

class _TestRefProcessor extends AnnotatedRefProcessor<_TestAnnotationSpec, _TestType, _TestContext> {

    private AnnotatedRef<_TestType> element;
    private _TestContext context;
    private boolean isInvoked;

    protected _TestRefProcessor() {
        super(r -> new _TestAnnotationSpec());
    }

    @Override
    protected void resolveValue(@NotNull final AnnotatedRef<_TestType> element, @NotNull final _TestContext context) {
        this.element = element;
        this.context = context;
        this.isInvoked = true;
    }

    public void verify(@NotNull final Consumer<AnnotatedRef<_TestType>> element, @NotNull final Consumer<_TestContext> context) {
        if (isInvoked) {
            element.accept(this.element);
            context.accept(this.context);
        } else {
            throw new AssertionError("Was not invoked.");
        }
    }

}
