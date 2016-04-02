package org.ametiste.lang;

import java.util.function.Consumer;

public interface Let<T> {

    void let(Consumer<T> value);

}
