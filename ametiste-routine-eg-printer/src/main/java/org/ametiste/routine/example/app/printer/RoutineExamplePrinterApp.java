package org.ametiste.routine.example.app.printer;

import org.ametiste.routine.configuration.AmetisteRoutineStarterConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 *
 * @since
 */
@SpringBootApplication
public class RoutineExamplePrinterApp {

    public static void main(String[] args) {
        SpringApplication.run(RoutineExamplePrinterApp.class, args);
    }

}
