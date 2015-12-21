package org.ametiste.routine.infrastructure.protocol.http;

import org.ametiste.routine.sdk.protocol.http.HttpProtocol;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 *
 * @since
 */
public class RestTemplateHttpConnection implements HttpProtocol {

    private final RestTemplate template;

    public RestTemplateHttpConnection() {
        this(new RestTemplate());
    }

    public RestTemplateHttpConnection(RestTemplate template) {
        this.template = template;
    }

    @Override
    public <T> T getObject(final String url, final Class<T> responseType) {
        return template.getForObject(url, responseType);
    }

    @Override
    public <T> T getObject(final String url, final Map<String, String> query, final Class<T> responseType) {
        return template.getForObject(url, responseType, query);
    }

    @Override
    public <T> T postObject(final String url, final Object body, final Class<T> responseType) {
        return template.postForObject(url, body, responseType);
    }

    @Override
    public <T> T postObject(final String url, final Map<String, String> query, final Object body, final Class<T> responseType) {
        return template.postForObject(url, body, responseType, query);
    }

}
