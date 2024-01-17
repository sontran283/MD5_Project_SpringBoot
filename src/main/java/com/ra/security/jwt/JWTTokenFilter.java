package com.ra.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private UserDetailsService userDetailsService;
    private final Logger logger= LoggerFactory.getLogger(JWTEntryPoint.class);


    // Phương thức doFilterInternal được override từ OncePerRequestFilter. Nó được gọi mỗi lần một request đi qua filter.
    // Kiểm tra xem request có chứa token và token đó có hợp lệ không bằng cách sử dụng jwtProvider.validateToken.
    // Nếu token hợp lệ, trích xuất tên người dùng từ token và tải thông tin chi tiết của người dùng thông qua userDetailsService.
    // Nếu thông tin chi tiết của người dùng tồn tại, tạo một đối tượng UsernamePasswordAuthenticationToken và đặt nó vào SecurityContextHolder.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String token = getTokenFromRequest(request);

            if (token != null && jwtProvider.validateToken(token)) {
                String userName = jwtProvider.getUserNameToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                System.out.println(userDetails);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }catch (Exception e){
            logger.error("UnAuthentications {}",e.getMessage());
        }
        filterChain.doFilter(request,response);
    }


    // Phương thức này trích xuất token từ header của request.
    // Header được kiểm tra có bắt đầu bằng chuỗi "Bearer " hay không.
    public String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}