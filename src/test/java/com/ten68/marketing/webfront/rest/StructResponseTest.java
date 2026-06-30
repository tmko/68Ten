package com.ten68.marketing.webfront.rest;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class StructResponseTest {

    @Test
    void constructor_setsStatusKeyValueAndNullException() {
        StructResponse response = new StructResponse(HttpStatus.OK, "key", "value");
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.key()).isEqualTo("key");
        assertThat(response.value()).isEqualTo("value");
        assertThat(response.exception()).isNull();
    }

    @Test
    void exceptionConstructor_setsInternalServerErrorAndStoresException() {
        Exception e = new RuntimeException("test error");
        StructResponse response = new StructResponse(e);
        assertThat(response.status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.key()).isEqualTo("Error");
        assertThat(response.value()).isEqualTo("test error");
        assertThat(response.exception()).isSameAs(e);
    }

    @Test
    void echo_createsOkResponseWithEchoKey() {
        StructResponse response = StructResponse.Echo("hello");
        assertThat(response.status()).isEqualTo(HttpStatus.OK);
        assertThat(response.key()).isEqualTo("Echo");
        assertThat(response.value()).isEqualTo("hello");
        assertThat(response.exception()).isNull();
    }

    @Test
    void singleObjectJsonRespond_returnsResponseEntityWithCorrectStatus() {
        StructResponse response = new StructResponse(HttpStatus.OK, "Echo", "test");
        ResponseEntity<String> entity = response.singleObjectJsonRespond();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void singleObjectJsonRespond_setsApplicationJsonContentType() {
        StructResponse response = StructResponse.Echo("test");
        ResponseEntity<String> entity = response.singleObjectJsonRespond();
        assertThat(entity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void singleObjectJsonRespond_bodyContainsEncodedValue() {
        StructResponse response = new StructResponse(HttpStatus.OK, "Echo", "hello world");
        ResponseEntity<String> entity = response.singleObjectJsonRespond();
        String encoded = URLEncoder.encode("hello world", StandardCharsets.UTF_8);
        assertThat(entity.getBody()).isEqualTo("{\"Echo\":\"%s\"}".formatted(encoded));
    }

    @Test
    void singleObjectJsonRespond_encodesSpecialCharacters() {
        StructResponse response = new StructResponse(HttpStatus.OK, "key", "a&b=c/d");
        ResponseEntity<String> entity = response.singleObjectJsonRespond();
        String encoded = URLEncoder.encode("a&b=c/d", StandardCharsets.UTF_8);
        assertThat(entity.getBody()).isEqualTo("{\"key\":\"%s\"}".formatted(encoded));
    }

    @Test
    void singleObjectJsonRespond_usesGivenHttpStatus() {
        StructResponse response = new StructResponse(HttpStatus.NOT_FOUND, "Error", "not found");
        ResponseEntity<String> entity = response.singleObjectJsonRespond();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void singleObjectJsonRespond_handlesEmptyValue() {
        StructResponse response = new StructResponse(HttpStatus.OK, "key", "");
        ResponseEntity<String> entity = response.singleObjectJsonRespond();
        assertThat(entity.getBody()).isEqualTo("{\"key\":\"nil\"}");
    }
}
