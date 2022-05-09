package projetosd.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(Map<String, Object> map, HttpStatus status) {
        return new ResponseEntity<Object>(map, status);
    }
}