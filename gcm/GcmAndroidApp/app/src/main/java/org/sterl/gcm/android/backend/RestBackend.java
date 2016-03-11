package org.sterl.gcm.android.backend;

import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Put;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Rest(converters = MappingJackson2HttpMessageConverter.class,
      requestFactory = RestSessionFactory.class,
      interceptors = RestAuthInterceptor.class)
public interface RestBackend {

    @Post("/gcmClient")
    void addClient(GcmClient token);
}
