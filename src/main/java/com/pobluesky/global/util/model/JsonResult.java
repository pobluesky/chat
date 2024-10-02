package com.pobluesky.global.util.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class JsonResult<T> {
    private final String result;
    private final T data;
    private final String message;

    // @JsonCreator 사용하여 Jackson이 이 생성자를 사용할 수 있도록 함
    @JsonCreator
    public JsonResult(
        @JsonProperty("result") String result,
        @JsonProperty("data") T data,
        @JsonProperty("message") String message
    ) {
        this.result = result;
        this.data = data;
        this.message = message;
    }

    // JsonResult 생성 메서드
    public static <T> JsonResult<T> of(String result, T data, String message) {
        return new JsonResult<>(result, data, message);
    }
}