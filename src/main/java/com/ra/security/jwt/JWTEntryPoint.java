package com.ra.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component  // Class JWTEntryPoint implements AuthenticationEntryPoint, đảm nhận vai trò xử lý sự kiện khi người dùng không được xác thực.
public class JWTEntryPoint implements AuthenticationEntryPoint {
    // được khởi tạo để ghi log. Log sẽ được ghi với tên của class AccessDenied.
    private final Logger logger= LoggerFactory.getLogger(JWTEntryPoint.class);
    @Override  // Phương thức được override từ AuthenticationEntryPoint để xử lý sự kiện khi có lỗi chưa được xác thực.
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Ghi log lỗi sử dụng logger đã khởi tạo trước đó.
        logger.error(authException.getMessage());
        // Tạo một ResponseEntity chứa thông điệp lỗi ("UnAuthentication") và mã trạng thái HTTP là UNAUTHORIZED (401).
        ResponseEntity<String> responseEntity=new ResponseEntity<>("UnAuthentication", HttpStatus.UNAUTHORIZED);
        // Đặt mã trạng thái HTTP của response bằng mã trạng thái của ResponseEntity.
        response.setStatus(responseEntity.getStatusCode().value());
        // Ghi thông điệp lỗi vào phản hồi HTTP. Objects.requireNonNull được sử dụng để đảm bảo rằng giá trị không null trước khi ghi vào phản hồi.
        response.getWriter().write(Objects.requireNonNull(responseEntity.getBody()));
    }
}
