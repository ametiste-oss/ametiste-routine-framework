package org.ametiste.lang;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.sun.tools.doclets.formats.html.markup.HtmlStyle.block;

/**
 *
 * @since
 */
public interface Peek<F, T> {

    T peek(Consumer<F> peeker);

    static <T, B> B fluent(Runnable block, Supplier<B> fluency) {
        block.run();
        return fluency.get();
    }

}
