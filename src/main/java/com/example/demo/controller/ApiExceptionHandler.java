package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// [7주차 연습문제 ②] API 오류 처리를 한 곳에 모은다
// assignableTypes : 관리자 API 에만 적용 (화면용 컨트롤러의 예외 처리에는 영향 없음)
@RestControllerAdvice(assignableTypes = AdminApiController.class)
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class) // 중복 아이디, 빈 값, 허용되지 않는 권한, 없는 회원, 본인 보호 …
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); // 400 {"error": "..."}
    }
}
