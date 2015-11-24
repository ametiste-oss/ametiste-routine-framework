package org.ametiste.routine.domain.task.notices;

import java.io.Serializable;
import java.time.Instant;

/**
 *
 * @since
 */
public class Notice implements Serializable {

    private final String text;

    private final Instant creationTime = Instant.now();

    public Notice(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }

    public Instant creationTime() {
        return creationTime;
    }

}
