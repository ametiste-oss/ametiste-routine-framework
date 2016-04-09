//package org.ametiste.routine.dsl.configuration.task.params;
//
//import org.ametiste.laplatform.protocol.ProtocolGateway;
//import org.ametiste.routine.dsl.annotations.TaskId;
//import org.ametiste.routine.dsl.annotations.TaskName;
//import org.ametiste.routine.dsl.application.AnnotatedParameterProvider;
//import org.ametiste.routine.meta.util.MetaMethodParameter;
//import org.ametiste.routine.sdk.protocol.operation.OperationMetaProtocol;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
///**
// * <p>
// *     Resolves task id for parameter marked as {@link OperationName}.
// * </p>
// *
// * <p>
// *     This provider using {@link OperationMetaProtocol} to resolve operation meta information.
// * </p>
// *
// * <p>
// *     Identifier can be resolved only as {@link String} type. Other paramter types are unsupported and
// *     will cause {@link IllegalStateException} at runtime.
// * </p>
// *
// * @see TaskName
// * @see OperationMetaProtocol
// * @since 1.1
// */
//@Component
//class TaskNameProvider extends AnnotatedParameterProvider {
//
//    public TaskNameProvider() {
//        super(TaskName.class);
//    }
//
//    @Override
//    protected Object resolveParameterValue(final MetaMethodParameter parameter,
//                                           final ProtocolGateway protocolGateway) {
//
//        if (parameter.type().equals(String.class)) {
//            return protocolGateway.session(OperationMetaProtocol.class).taskName();
//        } else {
//            throw new IllegalStateException("@TaskName parameter must have type of java.lang.String.");
//        }
//
//    }
//
//}
