package com.example.chattingonlineapplication.Webservice;

import android.util.Log;

import com.example.chattingonlineapplication.Webservice.Input.BaseInput;
import com.example.chattingonlineapplication.Webservice.Output.BaseOutput;
import com.example.chattingonlineapplication.Webservice.Provider.IHttpHeaderProvider;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

import bolts.Task;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class BaseClient {
    private IHttpHeaderProvider httpHeaderProvider;
    private OkHttpClient client;

    public BaseClient(IHttpHeaderProvider httpHeaderProvider) {
        this.httpHeaderProvider = httpHeaderProvider;
        this.client = new OkHttpClient.Builder().build();
    }

    protected abstract HttpUrl baseUrl();

    protected <T> Task<BaseOutput<T>> executeAsync(final BaseInput input, final Class<T> outputDataType) {
        return Task.callInBackground(new Callable<BaseOutput<T>>() {
            @Override
            public BaseOutput<T> call() {
                Response response = null;
                BaseOutput<T> output = new BaseOutput<>();
                ObjectMapper objectMapper = createObjectMapper();
                JsonFactory jsonFactory = new JsonFactory();
                try {
                    response = client.newCall(createRequest(input)).execute();
                    output.setStatusCode(response.code());
                    output.setStatusMessage(response.message());

                    if (response.isSuccessful()) {
                        JsonParser jsonParser = jsonFactory.createParser(response.body().string());
                        output.setData(objectMapper.readValue(jsonParser, outputDataType));
                        response.body().close();
                    }
                } catch (Exception e) {
                    output.setStatusCode(response.code());
                    output.setStatusMessage(e.getMessage() != null ? e.getMessage() : e.getClass().getName());
                    e.printStackTrace();
                }
                return output;
            }
        });
    }

    private Request createRequest(BaseInput input) {
        Request.Builder requestBuilder = new Request.Builder();


        // build headers
        if (httpHeaderProvider != null) {
            if (httpHeaderProvider.getHeaders() != null) {
                requestBuilder.headers(httpHeaderProvider.getHeaders());
            }
        }

        requestBuilder.url(buildHttpUrl(input));

        if (input.getMethod() != BaseInput.HTTPMethod.GET) {
            Log.i("Method", "OtherMethod" + input.getMethod());
        }
        return requestBuilder.build();
    }

    private HttpUrl buildHttpUrl(BaseInput input) {
        //Complete the base URl
        HttpUrl.Builder temHttpUrlBuilder = baseUrl().newBuilder();
        if (input.getResource().contains("/")) {
            for (String str : input.getResource().split("/")) {
                temHttpUrlBuilder.addPathSegment(str);
            }
        } else {
            temHttpUrlBuilder.addPathSegment(input.getResource());
        }
        HttpUrl httpUrl = temHttpUrlBuilder.build();
        HttpUrl.Builder fullUrlBuilder = httpUrl.newBuilder();

        for (Map.Entry<String, String> pathSegment : input.getPathSegments().entrySet()) {
            int index = httpUrl.pathSegments().indexOf(pathSegment.getKey());
            fullUrlBuilder.setPathSegment(index, pathSegment.getValue());
        }

        for (Map.Entry<String, String> queryParameter : input.getQueryParamters().entrySet()) {
            fullUrlBuilder.setQueryParameter(queryParameter.getKey(), queryParameter.getValue());
        }
        return fullUrlBuilder.build();
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        return mapper;
    }
}
