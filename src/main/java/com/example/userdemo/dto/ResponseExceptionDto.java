package com.example.userdemo.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ResponseExceptionDto {
    private final int code;
    private final String message;
}
