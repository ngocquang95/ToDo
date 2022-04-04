package com.demo.todo.common.exception;

import com.demo.todo.common.error.ErrorCode;

import java.io.Serial;

/**
 * Validation Error Exception
 */
public class ValidationErrorException extends CommonException
{
    @Serial
    private static final long serialVersionUID = 1L;
    private static final ErrorCode ERROR_CODE = ErrorCode.VALIDATION_ERROR;

    public ValidationErrorException(String messageTemplate, Object... args) {
        super(ERROR_CODE, messageTemplate, args);
    }
}
