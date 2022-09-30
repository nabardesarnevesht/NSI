package com.nabard.sarnevesht;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class MyHttpClient {
    public static final Interceptor  REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
                int maxAge = 60*60*10; // read from cache for 10 huors
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
        }

};
}
