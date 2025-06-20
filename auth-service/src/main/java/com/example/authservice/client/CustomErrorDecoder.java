package com.example.authservice.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.authservice.exception.GenericErrorResponse;
import com.example.authservice.exception.ValidationException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
            Map<String, String> errors =
                    mapper.readValue(IOUtils.toString(body, StandardCharsets.UTF_8), Map.class);

            if (response.status() == 400) {
                return ValidationException.builder()
                        .validationErrors(errors)
                        .build();
            } else {
                return GenericErrorResponse.builder()
                        .httpStatus(HttpStatus.valueOf(response.status()))
                        .message(errors.get("error"))
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