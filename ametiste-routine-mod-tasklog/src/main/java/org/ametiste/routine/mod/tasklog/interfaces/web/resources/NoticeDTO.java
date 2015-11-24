package org.ametiste.routine.mod.tasklog.interfaces.web.resources;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 *
 * @since
 */
public class NoticeDTO {

    private final String text;

    private final Instant time;

    public NoticeDTO(Instant time, String text) {
        this.time = time;
        this.text = text;
    }

    public String getTime() {
        return DateTimeFormatter.ISO_INSTANT.format(time);
    }

    public String getText() {
        return text;
    }

}
