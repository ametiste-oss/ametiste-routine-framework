package org.ametiste.routine.mod.backlog.configuration;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.mod.backlog.domain.RenewSchemeExecutor;
import org.ametiste.routine.mod.backlog.infrasturcture.*;
import org.ametiste.routine.mod.backlog.infrasturcture.BacklogTaskGateway;
import org.ametiste.routine.sdk.mod.DataGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

/**
 *
 * @since
 */
@Configuration
public class RenewSchemeExecutorConfiguration {

    @Autowired
    private Map<String, BacklogPopulationStrategy> backlogPopulationStrategies;

    @Autowired
    private TaskIssueService taskIssueService;

    @Bean
    public RenewSchemeExecutor renewSchemeExecutor() {
        return new DefaultRenewSchemeExecutor(
                backlogPopulationStrategiesRegistry(),
                new BacklogTaskGateway(taskIssueService),
                new DataGateway() {
                    @Override
                    public void storeModData(String name, String value) {

                    }

                    @Override
                    public void storeModData(String name, Integer value) {

                    }

                    @Override
                    public void storeModData(String name, Long value) {

                    }

                    @Override
                    public void storeModData(String name, Boolean value) {

                    }

                    @Override
                    public Optional<String> loadModData(String name) {
                        return Optional.empty();
                    }

                    @Override
                    public Optional<Integer> loadModDataInt(String name) {
                        return Optional.empty();
                    }

                    @Override
                    public Optional<Long> loadModDataLong(String name) {
                        return Optional.empty();
                    }

                    @Override
                    public Optional<Boolean> loadModDataBool(String name) {
                        return Optional.empty();
                    }
                }
        );
    }

    @Bean
    public BacklogPopulationStrategiesRegistry backlogPopulationStrategiesRegistry() {
        return new MemoryBacklogPopulationStrategiesRegistry(
                backlogPopulationStrategies
        );
    }

}
