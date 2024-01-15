package com.ra.advice;

import com.ra.exception.CustomException;
import com.ra.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // đánh dấu là một Advice (một component của Spring) được sử dụng để xử lý các exception trong ứng dụng
public class ApplicationHandler { // xử lý các exception cho các controller trong ứng dụng
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> invalidRequest(MethodArgumentNotValidException e) {
        // tạo một Map để lưu trữ các lỗi từ BindingResult
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)  //  annotation này sẽ được sử dụng để xử lý một loại exception cụ thể
    // khi một CustomException được ném ra trong ứng dụng, phương thức này sẽ được gọi để xử lý exception đó
    public ResponseEntity<String> customException(CustomException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserNotFoundException.class)  //  annotation này sẽ được sử dụng để xử lý một loại exception cụ thể
    // khi một CustomException được ném ra trong ứng dụng, phương thức này sẽ được gọi để xử lý exception đó
    public ResponseEntity<String> UserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
