package org.ametiste.routine.mod.backlog.configuration;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.configuration.AmetisteRoutineCoreConfiguration;
import org.ametiste.routine.infrastructure.mod.ModRepository;
import org.ametiste.routine.mod.backlog.domain.RenewSchemeExecutor;
import org.ametiste.routine.mod.backlog.infrastructure.*;
import org.ametiste.routine.mod.backlog.infrastructure.BacklogTaskGateway;
import org.ametiste.routine.sdk.mod.DataGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
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

    @Autowired
    private ModRepository modRepository;

    @Bean
    public RenewSchemeExecutor renewSchemeExecutor() {
        return new DefaultRenewSchemeExecutor(
                backlogPopulationStrategiesRegistry(),
                new BacklogTaskGateway(taskIssueService),
                new DataGateway() {
                    @Override
                    public void storeModData(String name, String value) {
                        modRepository.saveModProperty("mod-backlog", name, value);
                    }

                    @Override
                    public void storeModData(String name, Integer value) {
                        modRepository.saveModProperty("mod-backlog", name, Integer.toString(value));
                    }

                    @Override
                    public void storeModData(String name, Long value) {
                        modRepository.saveModProperty("mod-backlog", name, Long.toString(value));
                    }

                    @Override
                    public void storeModData(String name, Boolean value) {
                        modRepository.saveModProperty("mod-backlog", name, Boolean.toString(value));
                    }

                    @Override
                    public Optional<String> loadModData(String name) {
                        return modRepository.loadModProperty("mod-backlog", name);
                    }

                    @Override
                    public Optional<Integer> loadModDataInt(String name) {
                        return Optional.ofNullable(
                                modRepository.loadModProperty("mod-backlog", name)
                                        .map(Integer::valueOf)
                                        .orElse(null)
                        );
                    }

                    @Override
                    public Optional<Long> loadModDataLong(String name) {
                        return Optional.ofNullable(
                                modRepository.loadModProperty("mod-backlog", name)
                                        .map(Long::valueOf)
                                        .orElse(null)
                        );
                    }

                    @Override
                    public Optional<Boolean> loadModDataBool(String name) {
                        return Optional.ofNullable(
                                modRepository.loadModProperty("mod-backlog", name)
                                        .map(Boolean::valueOf)
                                        .orElse(null)
                        );
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
