//package org.ametiste.routine.dsl.configuration.task.params;
//
//import org.ametiste.laplatform.protocol.ProtocolGateway;
//import org.ametiste.routine.dsl.annotations.TaskId;
//import org.ametiste.routine.dsl.application.AnnotatedParameterProvider;
//import org.ametiste.routine.meta.util.MetaMethodParameter;
//import org.ametiste.routine.sdk.protocol.operation.OperationMetaProtocol;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
///**
// * <p>
// *     Resolves task id for parameter marked as {@link TaskId}.
// * </p>
// *
// * <p>
// *     This provider using {@link OperationMetaProtocol} to resolve operation meta information.
// * </p>
// *
// * <p>
// *     Identifier can be resolved as {@link String} or {@link UUID} type according to the
// *     type of the annotated parameter. Other paramter types are unsupported and
// *     will cause {@link IllegalStateException} at runtime.
// * </p>
// *
// * @see TaskId
// * @see OperationMetaProtocol
// * @since 1.1
// */
//@Component
//class TaskIdProvider extends AnnotatedParameterProvider {
//
//    public TaskIdProvider() {
//        super(TaskId.class);
//    }
//
//    @Override
//    protected Object resolveParameterValue(final MetaMethodParameter parameter,
//                                           final ProtocolGateway protocolGateway) {
//
//        final Object value;
//        final UUID taskId = protocolGateway.session(OperationMetaProtocol.class).taskId();
//
//        if (parameter.type().equals(UUID.class)) {
//            value = taskId;
//        } else if (parameter.type().equals(String.class)) {
//            value = taskId.toString();
//        } else {
//            throw new IllegalStateException("@TaskId parameter must have type of java.util.UUID or java.lang.String.");
//        }
//
//        return taskId;
//    }
//
//}
