package org.ametiste.routine.sdk.mod.protocol;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @since
 */
public interface ProtocolGateway {

    <T extends Protocol> T session(Class<T> protocolType);

    /**
     * <p>
     *      Sync variant of {@link #query(Consumer)} method, that installs given callback to the
     *      descriptor and blocks until query execution done. Returns consumed field value as raw {@code String}.
     * </p>
     *
     * <p>
     *     Note, provided QueryDescriptor should be opened, accept callback should not be installed to
     *     use this method.
     * </p>
     *
     * @param queryConsumer
     * @param callback
     *
     * @see GatewayMappers for details on builtin mappers
     */
//    String query(Consumer<QueryDescriptor> queryConsumer, GatewayCallback callback);

    /**
     * <p>
     * Sync variant of {@link #query(Consumer)} method, that installs given callback mapper to the
     * descriptor and blocks until query execution done. Returns consumed value mapped by the given
     * {@code callbackMapper}.
     * </p>
     *
     * <p>
     *     Note, provided QueryDescriptor should be opened, accept callback should not be installed to
     *     use this method.
     * </p>
     *
     * @param queryConsumer
     * @param callback
     *
     * @see GatewayMappers for details on builtin mappers
     */
//    <T> T query(Consumer<QueryDescriptor> queryConsumer, GatewayResponseMapper<T> callbackMapper);

}
