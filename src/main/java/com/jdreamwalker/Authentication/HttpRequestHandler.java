package main.java.com.jdreamwalker.Authentication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpRequestHandler extends RequestHandler {

    protected final RequestBuilder requestBuilder;

    protected final ResponseBuilder responseBuilder;

    public HttpRequestHandler(RequestBuilder requestBuilder, ResponseBuilder responseBuilder) {
        super(requestBuilder, responseBuilder);
        this.requestBuilder = requestBuilder;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public Boolean authorize(UserRequestDto userRequestDto) {

        HttpRequestDto httpRequestDto = (HttpRequestDto) requestBuilder.buildRequest(userRequestDto);
        try {
            // URL with query parameters
            URL url = new URL(httpRequestDto.getBaseUrl() + httpRequestDto.getApiPath() + buildQueryString(httpRequestDto.getQueryParams()));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpRequestDto.getHttpMethod());

            // Open connection
            if (httpRequestDto.getHeaderMap() != null) {
                for (Map.Entry<String, String> entry : httpRequestDto.getHeaderMap().entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            // Get response code
            int responseCode = connection.getResponseCode();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            connection.disconnect();

            return responseBuilder.buildResponse(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;

    }

    private String buildQueryString(Map<String, String> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return "";
        }

        StringBuilder queryString = new StringBuilder("?");
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            queryString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return queryString.substring(0, queryString.length() - 1); // Remove the last '&'
    }

}