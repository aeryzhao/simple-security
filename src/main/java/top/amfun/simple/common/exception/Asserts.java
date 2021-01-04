package top.amfun.simple.common.exception;

import top.amfun.simple.common.constant.ErrorCode;

/**
 * @date 2020/10/28 14:38
 * @description: 断言处理抛出Api异常
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(ErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
