package org.ametiste.routine.sdk.protocol.http;

import org.ametiste.laplatform.protocol.Protocol;

import java.util.Map;

/**
 * <p>
 *     Simple http protocol implementation, try mimic {@code RestTemplate} from springframework,
 *     but simplifies some aspects of the protocol.
 * </p>
 *
 * @since 0.1.0
 */
public interface HttpProtocol extends Protocol {

    <T> T getObject(String url, Class<T> responseType);

    <T> T getObject(String url, Map<String, String> query, Class<T> responseType);

    <T> T postObject(String url, Object body, Class<T> responseType);

    <T> T postObject(String url, Map<String, String> query, Object body, Class<T> responseType);

}
