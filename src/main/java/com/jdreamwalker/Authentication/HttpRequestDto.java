package main.java.com.jdreamwalker.Authentication;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpRequestDto extends RequestDto {

    @JsonAlias("base_url")
    private String baseUrl;
    @JsonAlias("api_path")
    private String apiPath;

    @JsonAlias("http_method")
    private String httpMethod;

    @JsonAlias("request_body")
    private Object requestBody;

    @JsonAlias("header_map")
    private Map<String, String> headerMap;
    @JsonAlias("query_params")
    private Map<String, String> queryParams;


    public HttpRequestDto(){

    }

    public HttpRequestDto(String baseUrl, String apiPath, Map<String, String> headerMap, String httpMethod,
                          Object requestBody, Map<String, String> queryParams) {
        this.baseUrl = baseUrl;
        this.apiPath = apiPath;
        this.headerMap = headerMap;
        this.httpMethod = httpMethod;
        this.requestBody = requestBody;
        this.queryParams = queryParams;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Object getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }


    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    @Override
    public String toString() {
        return "HttpRequestDto{" +
               "baseUrl='" + baseUrl + '\'' +
               ", apiPath='" + apiPath + '\'' +
               ", httpMethod='" + httpMethod + '\'' +
               ", requestBody=" + requestBody +
               ", headerMap=" + headerMap +
               ", queryParams=" + queryParams +
               '}';
    }
}