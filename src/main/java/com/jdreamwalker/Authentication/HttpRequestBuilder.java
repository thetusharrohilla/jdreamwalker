package main.java.com.jdreamwalker.Authentication;

public class HttpRequestBuilder extends RequestBuilder {

    public HttpRequestBuilder(final RequestContextBuilderAndDecorator requestContextBuilderAndDecorator){
        super(requestContextBuilderAndDecorator);
    }
    @Override
    public HttpRequestDto buildRequest(UserRequestDto userRequestDto) {
        HttpRequestDto httpRequestDto = new HttpRequestDto();
        requestContextBuilderAndDecorator.buildContextAndDecorate(httpRequestDto,userRequestDto);
        return httpRequestDto;
    }
}
