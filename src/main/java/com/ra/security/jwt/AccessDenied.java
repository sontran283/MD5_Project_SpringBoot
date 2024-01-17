package com.ra.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class AccessDenied implements AccessDeniedHandler {  // xử lý khi người dùng không có quyền truy cập.
    // được khởi tạo để ghi log. Log sẽ được ghi với tên của class AccessDenied.
    private final Logger logger= LoggerFactory.getLogger(AccessDenied.class);
    @Override  // Phương thức được override từ AccessDeniedHandler để xử lý sự kiện khi có lỗi Access Denied.
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Ghi log lỗi sử dụng logger đã khởi tạo trước đó.
        logger.error("UnPermission {}",accessDeniedException.getMessage());
        // Tạo một ResponseEntity chứa thông điệp lỗi ("UnPermission") và mã trạng thái HTTP là FORBIDDEN (403).
        ResponseEntity<String> responseEntity=new ResponseEntity<>("UnPermission", HttpStatus.FORBIDDEN);
        // Đặt mã trạng thái HTTP của response bằng mã trạng thái của ResponseEntity.
        response.setStatus(responseEntity.getStatusCode().value());
        // Ghi thông điệp lỗi vào phản hồi HTTP. Objects.requireNonNull được sử dụng để đảm bảo rằng giá trị không null trước khi ghi vào phản hồi.
        response.getWriter().write(Objects.requireNonNull(responseEntity.getBody()));
    }
}
