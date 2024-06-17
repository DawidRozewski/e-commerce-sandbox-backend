package com.dawidrozewski.sandbox.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Date;

@AllArgsConstructor
@Getter
public class DefaultErrorDto {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
