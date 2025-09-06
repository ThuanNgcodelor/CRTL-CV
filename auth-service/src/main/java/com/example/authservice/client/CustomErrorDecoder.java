package com.example.authservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.authservice.exception.GenericErrorResponse;
import com.example.authservice.exception.ValidationException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.body() == null) {
            return GenericErrorResponse.builder()
                    .httpStatus(HttpStatus.valueOf(response.status()))
                    .message("No error body returned from server")
                    .build();
        }

        try (InputStream body = response.body().asInputStream()) {
            String responseBody = IOUtils.toString(body, StandardCharsets.UTF_8);
            log.error("Error response from {}: {}", methodKey, responseBody);
            
            try {
                Map<String, Object> errors = mapper.readValue(responseBody, Map.class);
                if (response.status() == 400) {
                    // Try to extract field errors if present; fall back to generic map
                    return ValidationException.builder()
                            .validationErrors((Map) errors.getOrDefault("errors", errors))
                            .build();
                } else {
                    String msg = (errors.get("error") != null ? errors.get("error").toString()
                            : (errors.get("message") != null ? errors.get("message").toString()
                            : "Unknown error"));
                    return GenericErrorResponse.builder()
                            .httpStatus(HttpStatus.valueOf(response.status()))
                            .message(msg)
                            .build();
                }
            } catch (Exception e) {
                return GenericErrorResponse.builder()
                        .httpStatus(HttpStatus.valueOf(response.status()))
                        .message("Error response: " + responseBody)
                        .build();
            }

        } catch (IOException exception) {
            return GenericErrorResponse.builder()
                    .httpStatus(HttpStatus.valueOf(response.status()))
                    .message(exception.getMessage())
                    .build();
        }
    }
}