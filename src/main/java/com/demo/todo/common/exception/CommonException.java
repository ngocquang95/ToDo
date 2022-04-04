package com.demo.todo.common.exception;

import com.demo.todo.common.error.ErrorCode;
import lombok.Getter;

/**
 * Common Exception
 */
public class CommonException extends RuntimeException
{
    @Getter
    private final ErrorCode code;

    public CommonException(ErrorCode code, String format, Object... args) {
        super(String.format(format, args));
        this.code = code;
    }
}
