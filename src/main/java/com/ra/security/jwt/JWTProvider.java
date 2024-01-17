package com.ra.security.jwt;

import com.ra.security.user_principle.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTProvider {
    // Các trường (fields) EXPIRED và SECRET_KEY được chú thích bằng @Value để lấy giá trị từ file cấu hình.
    @Value("${expired}")
    private Long EXPIRED;
    @Value("${secret_key}")
    private String SECRET_KEY;
    private final Logger logger= LoggerFactory.getLogger(JWTEntryPoint.class);

    // Phương thức này tạo ra một JWT token dựa trên thông tin của người dùng (UserPrinciple).
    // Jwts.builder() bắt đầu quá trình tạo token.
    // setSubject đặt chủ đề của token là tên người dùng.
    // setIssuedAt đặt thời điểm phát hành là thời điểm hiện tại.
    // setExpiration đặt thời điểm token hết hạn dựa trên giá trị EXPIRED.
    // signWith sử dụng thuật toán HS256 để ký (sign) token với SECRET_KEY.
    // compact kết thúc quá trình xây dựng và trả về chuỗi token.
    public String generateToken(UserPrinciple userPrinciple) {
        return Jwts.builder()
                .setSubject(userPrinciple.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRED))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }


    // Phương thức này xác thực một token đã cho.
    // Nếu token hợp lệ, không có lỗi, nó trả về true.
    // Nếu có lỗi, các loại lỗi khác nhau được log và nó trả về false.
    public Boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException expiredJwtException){
            logger.error("Expired Token Out {}",expiredJwtException.getMessage());
        }catch (MalformedJwtException malformedJwtException){
            logger.error("Invalid format {}",malformedJwtException.getMessage());
        }catch (UnsupportedJwtException unsupportedJwtException){
            logger.error("Unsupported Token {}",unsupportedJwtException.getMessage());
        }catch (SignatureException signatureException){
            logger.error("Wrong Signature {}",signatureException.getMessage());
        }
        return false;
    }

    // Phương thức này trích xuất tên người dùng từ một token đã cho.
    public String getUserNameToken(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
}
