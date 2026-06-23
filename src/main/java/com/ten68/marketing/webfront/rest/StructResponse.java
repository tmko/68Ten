package com.ten68.marketing.webfront.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public record StructResponse (
    HttpStatus status,
    String key,
    String value,
    Exception exception
){

    public StructResponse (HttpStatus status, String key, String value) {
        this(status, key, value, null);
    }

    public StructResponse (Exception e) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, "Error", e.getMessage(), e);
    }


    public ResponseEntity<String> singleObjectJsonRespond() {
        String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
        String simpleSingleJSON = "{\"%s\":\"%s\"}".formatted(key,encodedValue);
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(simpleSingleJSON);
    }

}
