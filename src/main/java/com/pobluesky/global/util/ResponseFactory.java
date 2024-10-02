package com.pobluesky.global.util;

import com.pobluesky.global.util.model.CommonResult;
import com.pobluesky.global.util.model.JsonResult;

public class ResponseFactory {

    // CommonResult 관련 메서드
    public static CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    public static CommonResult getFailResult(String code, String msg) {
        CommonResult result = new CommonResult();
        setFailResult(result, code, msg);
        return result;
    }

    private static void setSuccessResult(CommonResult result) {
        result.setCode("SUCCESS");
        result.setMessage("성공하였습니다.");
    }

    private static void setFailResult(CommonResult result, String code, String msg) {
        result.setCode(code);
        result.setMessage(msg);
    }

    // JsonResult 관련 메서드
    public static <T> JsonResult<T> getSuccessJsonResult(T data) {
        return JsonResult.of("success", data, "성공하였습니다.");
    }

    public static <T> JsonResult<T> getFailJsonResult(String message) {
        return JsonResult.of("fail", null, message);
    }
}