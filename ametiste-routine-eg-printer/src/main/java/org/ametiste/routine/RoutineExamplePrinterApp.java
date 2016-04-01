package org.ametiste.routine;

import org.ametiste.laplatform.protocol.GatewayContext;
import org.ametiste.laplatform.protocol.Protocol;
import org.ametiste.laplatform.protocol.ProtocolFactory;
import org.ametiste.routine.domain.scheme.StatelessOperationScheme;
import org.ametiste.routine.mod.shredder.configuration.ModShredderConfiguration;
import org.ametiste.routine.printer.operation.PrintExecutor;
import org.ametiste.routine.printer.operation.PrintOperationParams;
import org.ametiste.routine.printer.scheme.PrintTaskSchemeParams;
import org.ametiste.routine.printer.scheme.PrintTaskScheme;
import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @since
 */
@SpringBootApplication
@Import(ModShredderConfiguration.class)
public class RoutineExamplePrinterApp {

    public static void main(String[] args) {
        SpringApplication.run(RoutineExamplePrinterApp.class, args);
    }

    @Autowired
    private ApplicationContext context;

    @Bean
    public Backlog backlog() {
        return new Backlog(PrintTaskScheme.NAME, PrintTaskScheme.NAME + "-population");
    }

    @Bean
    @Scope(scopeName = "prototype")
    public PrintOperationParams printOperationParamsProtocol(GatewayContext c) {
        return new PrintOperationParams(c.lookupMap("params"));
    }

    @Bean
    public ProtocolFactory<PrintOperationParams> printOperationParamsProtocolConnectionFactory() {
        return c -> printOperationParamsProtocol(c);
    }

    @Bean
    @Scope(scopeName = "prototype")
    public PrintTaskSchemeParams printTaskSchemeParamsProtocol(GatewayContext c) {
        return new PrintTaskSchemeParams(c.lookupMap("params"));
    }

    @Bean
    public ProtocolFactory<PrintTaskSchemeParams> printTaskSchemeParamsProtocolConnectionFactory() {
        return c -> printTaskSchemeParamsProtocol(c);
    }

}
