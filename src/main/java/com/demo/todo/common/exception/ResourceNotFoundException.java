package com.demo.todo.common.exception;

import com.demo.todo.common.error.ErrorCode;

import java.io.Serial;

/**
 * Resource Not Found Exception
 */
public class ResourceNotFoundException extends CommonException
{
    @Serial
    private static final long serialVersionUID = 1L;
    private static final ErrorCode ERROR_CODE = ErrorCode.RESOURCE_NOT_FOUND;
    private static final String MESSAGE_TEMPLATE = "Resource '%s' specified id = '%s' does not exists.";

    public ResourceNotFoundException(Class<?> resourceClass, Long id) {
        super(ERROR_CODE, MESSAGE_TEMPLATE, resourceClass.getSimpleName(), id);
    }
}
