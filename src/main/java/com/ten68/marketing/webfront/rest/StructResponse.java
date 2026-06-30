package com.ten68.marketing.webfront.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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

    public static StructResponse Echo (String input) {
        return new StructResponse(HttpStatus.OK, "Echo", input);
    }

    public ResponseEntity<String> singleObjectJsonRespond() {
        String k = nonEmpty(key);
        String v = nonEmpty(value);
        String encodedValue = URLEncoder.encode(v, StandardCharsets.UTF_8);
        String simpleSingleJSON = "{\"%s\":\"%s\"}".formatted(k,encodedValue);
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(simpleSingleJSON);
    }

    private static String nonEmpty (String s) {
        return ( s == null || s.isBlank() ) ? "nil" : s;
    }
}
