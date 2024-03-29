package com.example.custom.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //第一步，获得chain内的request
        Request request = chain.request();
        //第二步，用chain执行request
        Response response = chain.proceed(request);

        //第三步，返回response
//        return response;
        return response.newBuilder().body(new BaseResponseBody(response.body())).build();
    }
}
