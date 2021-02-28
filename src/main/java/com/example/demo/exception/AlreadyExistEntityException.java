package com.example.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
@Getter
public class AlreadyExistEntityException extends ResponseStatusException {

    private final List<String> fields = new ArrayList<>();
    private final String message = "Duplicated ";

    public AlreadyExistEntityException(String ...field) {
        super(HttpStatus.UNPROCESSABLE_ENTITY);
        fields.addAll(Arrays.asList(field));
    }

    @Override
    public String getMessage() {
        if (fields.isEmpty()) return message;
        return message + fields.toString();
    }
}
