package org.sterl.gcm.android.backend;

import android.os.Build;

import org.androidannotations.annotations.Bean;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class RestAuthInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        // http://stackoverflow.com/questions/13182519/spring-rest-template-usage-causes-eofexception
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            headers.set("Connection", "Close");
        }
        return execution.execute(request, body);
    }
}