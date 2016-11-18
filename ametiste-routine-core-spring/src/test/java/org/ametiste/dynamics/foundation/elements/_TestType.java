package org.ametiste.dynamics.foundation.elements;

@_TestAnnotation
public class _TestType {

    @_TestAnnotation
    private String testStringField;

    private Object testObjectField;

    public void testUnannotatedMethod() {

    }

    @_TestAnnotation
    public void testVoidMethod() {

    }

    @_TestAnnotation
    public String testStringMethod() {
        return "Hello, Test!";
    }

    @_TestAnnotation
    private String testPrivateStringMethod() {
        return "Hello, private Test!";
    }

}
