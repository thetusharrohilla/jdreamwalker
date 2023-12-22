package main.java.com.jdreamwalker.Authentication;

public abstract class RequestBuilder {

    protected final RequestContextBuilderAndDecorator requestContextBuilderAndDecorator;

    public RequestBuilder(final RequestContextBuilderAndDecorator requestContextBuilderAndDecorator){
        this.requestContextBuilderAndDecorator = requestContextBuilderAndDecorator;
    }


    public abstract RequestDto buildRequest(UserRequestDto userRequestDto);

}
