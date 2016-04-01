package org.ametiste.routine;

import org.ametiste.routine.mod.shredder.configuration.ModShredderConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 *
 * @since
 */
@SpringBootApplication
@Import(ModShredderConfiguration.class)
public class RoutineExampleDSLPrinterApp {

    public static void main(String[] args) {
        SpringApplication.run(RoutineExampleDSLPrinterApp.class, args);
    }

}
