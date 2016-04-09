package org.ametiste.routine.dsl.configuration.protocol;

import org.ametiste.lang.Pair;
import org.ametiste.laplatform.dsl.LambdaProtocol;
import org.ametiste.laplatform.dsl.ProtocolMeta;
import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.laplatform.sdk.protocol.Protocol;
import org.ametiste.routine.dsl.infrastructure.DynamicProtocolMetricsTool;
import org.ametiste.routine.meta.util.MetaMethod;
import org.ametiste.routine.meta.util.MetaObject;
import org.ametiste.routine.sdk.mod.ModGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * <p>
 * <b>Dynamic Protocols Metrics Feature</b> is implemented as custom
 * {@link org.ametiste.laplatform.protocol.gateway.ProtocolGatewayTool}, which implementation
 * can be found at {@link DynamicProtocolMetricsTool}.
 * </p>
 *
 * @since
 * @see DynamicProtocolMetricsTool
 * @since 1.1
 */
@Configuration
public class ProtocolDSLConfiguration {

    @Autowired(required = false)
    @LambdaProtocol
    private List<Protocol> lambdaProtocols = Collections.emptyList();

    @Autowired
    private ProtocolGatewayService protocolGatewayService;

    @Autowired
    private ApplicationContext applicationContext;

    // TODO: can I do it with bean post processor?
    @Bean
    public ModGateway modDSLProtocol() {

        final List<? extends Class<? extends Protocol>> protocolClasses = lambdaProtocols.stream()
                .map(Protocol::getClass)
                .collect(Collectors.toList());

        protocolClasses.stream()
                .map(this::protoGatewayEntry)
                .forEach(protocolGatewayService::registerProtocol);

        return gw -> {
            // TODO: how can I propagate artifact version?
            gw.modInfo("dsl-protocol", "1.1",
                protocolClasses.stream().collect(Collectors.toMap(s -> s.getName(), s -> ""))
            );
        };
    }

    private ProtocolGatewayService.Entry protoGatewayEntry(Class<? extends Protocol> protocolClass) {

        if (!protocolClass.isAnnotationPresent(LambdaProtocol.class)) {
            throw new IllegalArgumentException("Only @LambdaProtocol classes are allowed.");
        }

        final String protocolName = resolveProtocolName(protocolClass);
        final String protocolGroup = resolveProtocolGroup(protocolClass);
        final Class<? extends Protocol> protocolType = (Class<? extends Protocol>)protocolClass.getInterfaces()[0];
        final Map<String, String> operationsMapping = resolveOperationNamesMapping(protocolClass);

        return new ProtocolGatewayService.Entry(protocolName, protocolGroup, operationsMapping, protocolType, c -> {
            try {
                final Protocol protocol = (Protocol) protocolClass.newInstance();
                applicationContext.getAutowireCapableBeanFactory().autowireBean(protocol);
                return protocol;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static String resolveProtocolName(final Class<? extends Protocol> protocolClass) {

        final String protocolName;
        final String shortName;

        if (protocolClass.isAnnotationPresent(ProtocolMeta.class)) {
            protocolName = protocolClass.getDeclaredAnnotation(ProtocolMeta.class).shortName();
        } else {
            // TODO: I want to have this limitation as part of LP, as customizable constraint
            if (protocolClass.getSimpleName().length() > 16) {
                throw new IllegalArgumentException("Error during protocol " + protocolClass.getName() + " registration" +
                        "Generated protocol shortname " +
                        "length can't be greater than 16. Please use @ProtocolMeta(shortName=) " +
                        "annotation parameter to avoid generated limitations.");
            }
            protocolName = splitCamelCase(protocolClass.getSimpleName());
        }

        return protocolName;
    }

    private static String resolveProtocolGroup(final Class<? extends Protocol> protocolClass) {

        final String protocolGroup;

        if (protocolClass.isAnnotationPresent(ProtocolMeta.class)) {
            protocolGroup = protocolClass.getDeclaredAnnotation(ProtocolMeta.class).group();
        } else {
            protocolGroup = "infrastructure.protocol";
        }

        return protocolGroup;
    }

    private static Map<String, String> resolveOperationNamesMapping(final Class<? extends Protocol> protocolClass) {

        final Map<String, String> generatedOpsNaming = MetaObject.from(protocolClass)
                .streamOfMethodsWithoutAnnotation(ProtocolMeta.class)
                .filter(m -> filterObjectNativeMethods(m.name()))
                .map(m -> Pair.of(m.name(), resolveDefaultOperationName(protocolClass, m)))
                .collect(Collectors.toMap(p -> p.first, p -> p.second));

        final Map<String, String> definedOpsNames = MetaObject.from(protocolClass)
                .streamOfAnnotatedMethods(ProtocolMeta.class)
                .map(m -> Pair.of(m.name(), m.annotationValue(ProtocolMeta.class, ProtocolMeta::shortName).get()))
                .collect(Collectors.toMap(p -> p.first, p -> p.second));

        final Map<String, String> operationsMapping = new HashMap<>();
        operationsMapping.putAll(definedOpsNames);
        operationsMapping.putAll(generatedOpsNaming);

        return operationsMapping;
    }

    private static String resolveDefaultOperationName(Class<?> protocolClass, MetaMethod method) {
        // TODO: I want to have this limitation as part of LP, as customizable constraint
        if (method.name().length() > 16) {
            throw new IllegalArgumentException(
                    "Error during protocol " + protocolClass.getName() + " operation " + method.name() + " registration. " +
                    "Generated operation shortname length can't be greater than 16. Please use @ProtocolMeta(shortName=) " +
                    "annotation parameter to avoid generated limitations.");
        }

        return splitCamelCase(method.name());
    }

    // TODO: atm native object methods like "wait" and "notify" will be mapped as "null" operation name
    // I want to exclude these methods from metrics at all. I guess I need some
    // kind of filter in the SessionStatOption at the pgw.
    private static List<String> excludeMethods = Arrays.asList("wait", "notify");

    private static boolean filterObjectNativeMethods(String methodName) {
        return !excludeMethods.contains(methodName);
    }

    private static String splitCamelCase(String s) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1-$2";
        return s.replaceAll(regex, replacement)
                .toLowerCase();
    }

}