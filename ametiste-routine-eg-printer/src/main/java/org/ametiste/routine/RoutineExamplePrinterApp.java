package org.ametiste.routine;

import org.ametiste.routine.printer.scheme.PrintTaskScheme;
import org.ametiste.routine.mod.backlog.configuration.BacklogModConfiguration;
import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @since
 */
@SpringBootApplication
@EnableScheduling
@Import(BacklogModConfiguration.class)
public class RoutineExamplePrinterApp {

    public static void main(String[] args) {
        SpringApplication.run(RoutineExamplePrinterApp.class, args);
    }

    @Bean
    public Backlog backlog() {
        return new Backlog(PrintTaskScheme.NAME, PrintTaskScheme.NAME + "-population");
    }

}
