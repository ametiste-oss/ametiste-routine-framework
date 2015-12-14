package org.ametiste.routine.sdk.mod.protocol;

/**
 *
 * @since
 */
public interface ProtocolFamily {

    ProtocolGateway createGateway(String protoName /*TODO: add context holder supplier */);

}
