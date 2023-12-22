package main.java.com.jdreamwalker.Authentication;

public interface RequestContextBuilderAndDecorator {
    void buildContextAndDecorate(HttpRequestDto httpRequestDto , UserRequestDto userRequestDto);
}
