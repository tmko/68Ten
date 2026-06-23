package com.ten68.marketing.webfront.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class StructResponse {
    private final HttpStatus status;
    private final String key;
    private final String value;
    private final Exception exception;

    public StructResponse (HttpStatus status, String key, String value) {
        this.status = status;
        this.key = key;
        this.value = value;
        this.exception = null;
    }

    public StructResponse (Exception e) {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.key = "Error";
        this.value = e.getMessage();
        this.exception = e;
    }


    public ResponseEntity<String> oneLineJsonRespond() {
        String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
        String simpleSingleJSON = "{\"%s\":\"%s\"}".formatted(key,encodedValue);
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(simpleSingleJSON);
    }

}
