package com.increff.pos.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UploadException extends Exception {
    private List<List<String>> errors;
    private String message;
    public UploadException(String message, List<List<String>> errors) {
        this.message = message;
        this.errors = errors;
    }
    public UploadException(String message) {
        this.message= message;
    }
}
