package org.sterl.gcm.android.backend;


import android.util.Log;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.sterl.gcm.android.R;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@EBean
public class RestSessionFactory implements ClientHttpRequestFactory {
    @StringRes(R.string.backend_url) String backendUrl;
    private SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

    @Override
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        final URI serverUri = URI.create(backendUrl + uri.toString());
        Log.d("RestSessionFactory", serverUri.toString());
        requestFactory.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(10));
        return requestFactory.createRequest(serverUri, httpMethod);
    }
}
