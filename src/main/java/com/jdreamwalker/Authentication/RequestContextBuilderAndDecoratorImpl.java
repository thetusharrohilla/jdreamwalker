package main.java.com.jdreamwalker.Authentication;

import main.java.com.jdreamwalker.util.TransformUtil;

import java.util.HashMap;
import java.util.Map;

public class RequestContextBuilderAndDecoratorImpl implements RequestContextBuilderAndDecorator {

    private String baseUrl;
    private String apiPath;
    private String httpMethod;

    Map<String, String> headers;

    HttpRequestDto httpRequestDto = new HttpRequestDto();

    public RequestContextBuilderAndDecoratorImpl(Object httpRequestInitializerObject){
        this.httpRequestDto = TransformUtil.convertValue(httpRequestInitializerObject , HttpRequestDto.class);
    }

    @Override
    public void buildContextAndDecorate(HttpRequestDto httpRequestDto, UserRequestDto userRequestDto) {
        httpRequestDto.setBaseUrl(this.httpRequestDto.getBaseUrl());
        httpRequestDto.setApiPath(this.httpRequestDto.getApiPath());
        httpRequestDto.setHttpMethod(this.httpRequestDto.getHttpMethod());
        httpRequestDto.setHeaderMap(this.httpRequestDto.getHeaderMap());
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("auth_token", userRequestDto.getHeaderMap().get("Auth_token"));
        httpRequestDto.setQueryParams(queryParams);
    }
}
