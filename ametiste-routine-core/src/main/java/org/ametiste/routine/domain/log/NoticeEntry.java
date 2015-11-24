package org.ametiste.routine.domain.log;

import java.time.Instant;

/**
 *
 * @since
 */
public class NoticeEntry {

    private final String text;

    private final Instant time;

    public NoticeEntry(Instant time, String text) {
        this.time = time;
        this.text = text;
    }

    public Instant getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

}
