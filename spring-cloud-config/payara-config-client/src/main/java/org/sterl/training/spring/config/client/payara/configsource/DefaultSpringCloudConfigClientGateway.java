package org.sterl.training.spring.config.client.payara.configsource;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Copy more or less from: 
 * https://github.com/quarkusio/quarkus/blob/master/extensions/spring-cloud-config-client/runtime/src/main/java/io/quarkus/spring/cloud/config/client/runtime/DefaultSpringCloudConfigClientGateway.java
 */
class DefaultSpringCloudConfigClientGateway implements AutoCloseable {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final SpringCloudConfigClientConfig springCloudConfigClientConfig;
    private final URI baseURI;
    private HttpClientBuilder clientBuilder;
    private CloseableHttpClient client;
    /** Authorization Header */
    private final String authz;
    
    DefaultSpringCloudConfigClientGateway(SpringCloudConfigClientConfig springCloudConfigClientConfig) {
        this.springCloudConfigClientConfig = springCloudConfigClientConfig;
        try {
            if (this.springCloudConfigClientConfig.usernameAndPasswordSet()) {
                this.authz = "Basic " + springCloudConfigClientConfig.buildBasicAuthz();
            } else {
                this.authz = null;
            }
            this.baseURI = determineBaseUri(springCloudConfigClientConfig);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Value: '" + springCloudConfigClientConfig.url
                    + "' of property 'spring-cloud-config.url' is invalid", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    Response exchange(String applicationName, String profile) throws Exception {
        
        try {
            CloseableHttpClient client = getHttpClient();
            final URI finalURI = finalURI(applicationName, profile);
            final HttpGet request = new HttpGet(finalURI);
            request.addHeader(HttpHeaders.ACCEPT, "application/json");
            if (authz != null) request.addHeader(HttpHeaders.AUTHORIZATION, authz);

            try (CloseableHttpResponse response = client.execute(request)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException("Got unexpected HTTP response code " + response.getStatusLine().getStatusCode()
                            + " from " + finalURI);
                }
                final HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new RuntimeException("Got empty HTTP response body " + finalURI);
                }

                return OBJECT_MAPPER.readValue(EntityUtils.toString(entity), Response.class);
            }
        } catch (Exception e) {
            this.close();
            throw e;
        }
    }
    
    private CloseableHttpClient getHttpClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        if (this.clientBuilder == null) {
            final SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (x509CertChain, authType) -> true)
                    .build();

            final RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout((int) this.springCloudConfigClientConfig.connectionTimeout.toMillis())
                    .setSocketTimeout((int) this.springCloudConfigClientConfig.readTimeout.toMillis())
                    .build();
            clientBuilder = HttpClientBuilder.create()
                    .setSSLContext(sslContext)
                    .setConnectionManagerShared(true) // allow the close method
                    .setConnectionManager(
                        new PoolingHttpClientConnectionManager(
                            RegistryBuilder.<ConnectionSocketFactory>create()
                                .register("http", PlainConnectionSocketFactory.INSTANCE)
                                .register("https", new SSLConnectionSocketFactory(sslContext,
                                        NoopHostnameVerifier.INSTANCE))
                                .build()
                        )
                    )
                    .setDefaultRequestConfig(requestConfig);
        }
        if (this.client == null) {
            this.client = clientBuilder.build();
        }
        return client;
    }
    
    private URI determineBaseUri(SpringCloudConfigClientConfig springCloudConfigClientConfig) throws URISyntaxException {
        String url = springCloudConfigClientConfig.url;
        if (null == url || url.isEmpty()) {
            throw new IllegalArgumentException(
                    "The 'spring-cloud-config.url' property cannot be empty");
        }
        if (url.endsWith("/")) {
            return new URI(url.substring(0, url.length() - 1));
        }
        return new URI(url);
    }

    private URI finalURI(String applicationName, String profile) throws URISyntaxException {
        URIBuilder result = new URIBuilder(this.baseURI);
        if (result.getPort() == -1) {
            // we need to set the port otherwise auth case doesn't match the request
            result.setPort(result.getScheme().equalsIgnoreCase("http") ? 80 : 443);
        }
        final List<String> finalPathSegments = new ArrayList<>(result.getPathSegments());
        finalPathSegments.add(applicationName);
        finalPathSegments.add(profile);
        result.setPathSegments(finalPathSegments);
        return result.build();
    }

    @Override
    public void close() throws Exception {
        // reuse of the client is maybe overkill, we could re-create for each request
        if (client != null) {
            client.close();
            client = null;
        }
    }
}
