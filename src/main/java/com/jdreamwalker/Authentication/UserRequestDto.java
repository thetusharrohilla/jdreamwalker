package main.java.com.jdreamwalker.Authentication;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequestDto {
    private Object requestBody;

    private Map<String, String> headerMap;

    private Map<String, String> queryParams;

    public Object getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }
    public UserRequestDto() {
    }

    public UserRequestDto(Object requestBody, Map<String, String> headerMap, Map<String, String> queryParams) {
        this.requestBody = requestBody;
        this.headerMap = headerMap;
        this.queryParams = queryParams;
    }

    @Override
    public String toString() {
        return "UserRequestDto{" +
               "requestBody=" + requestBody +
               ", headerMap=" + headerMap +
               ", queryParams=" + queryParams +
               '}';
    }
}