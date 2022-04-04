package com.demo.todo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Status
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Status
{
    /**
     * Planing Status
     */
    PLANNING(0),

    /**
     * Doing Status
     */
    DOING(1),

    /**
     * Complete Status
     */
    COMPLETE(2);

    @Getter(onMethod = @__(@JsonValue))
    private final Integer value;

    /**
     * Check status
     * @param value Integer
     * @return Status
     */
    @JsonCreator
    public static Status of(Integer value) {
        return Arrays.stream(values())
                .filter(v -> v.getValue().equals(value))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Status = '" + value + "' is not supported."));
    }
}
