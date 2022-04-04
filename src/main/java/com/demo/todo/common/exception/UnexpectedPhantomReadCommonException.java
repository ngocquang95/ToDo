package com.demo.todo.common.exception;

import com.demo.todo.common.error.ErrorCode;

import java.io.Serial;

/**
 * Unexpected Phantom Read Common Exception
 */
public class UnexpectedPhantomReadCommonException extends CommonException
{
    @Serial
    private static final long serialVersionUID = 1L;
    private static final ErrorCode ERROR_CODE = ErrorCode.INTERNAL_SERVER_ERROR;
    private static final String MESSAGE_TEMPLATE = "Unexpected phantom read has occurred.";

    public UnexpectedPhantomReadCommonException() {
        super(ERROR_CODE, MESSAGE_TEMPLATE);
    }
}
