package com.demo.todo.common.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Paging parameter
 */
@Getter
@Setter
public class PaginationParam
{
    /**
     * Page number (default 0)
     */
    private Integer page = 0;

    /**
     * Page limit (default 20)
     */
    private Integer limit = 20;
}
