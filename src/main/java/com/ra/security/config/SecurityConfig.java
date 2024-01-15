package com.ra.security.config;

import com.ra.security.jwt.AccessDenied;
import com.ra.security.jwt.JWTEntryPoint;
import com.ra.security.jwt.JWTTokenFilter;
import com.ra.security.user_principle.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration  // đánh dấu nó như một class cấu hình trong Spring, chứa các cấu hình và các bean được khởi tạo cho ứng dụng
@EnableWebSecurity // để kích hoạt tính năng bảo mật web trong ứng dụng, Nó thông báo cho Spring rằng ứng dụng sử dụng Spring Security để xác thực và ủy quyền người dùng trên mạng
@EnableMethodSecurity // để xác thực và ủy quyền trên mức độ phương thức
public class SecurityConfig {
    @Autowired
    private UserDetailService userDetailService; // xác thực người dùng
    @Autowired
    private JWTTokenFilter jwtTokenFilter; // kiểm tra và xác thực token JWT trong các yêu cầu
    @Autowired
    private JWTEntryPoint jwtEntryPoint;  // để xử lý xác thực không thành công
    @Autowired
    private AccessDenied accessDenied;  //  để xử lý truy cập bị từ chối

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)  // vô hiệu hóa CSRF (Cross-Site Request Forgery) trong httpSecurity
                .authenticationProvider(authenticationProvider()) // cung cấp một authenticationProvider để xác thực người dùng
                .authorizeHttpRequests(  // cấu hình việc ủy quyền truy cập cho các yêu cầu HTTP
                        (auth) -> auth
                                .requestMatchers("/auth/**", "/uploads/**", "/products/**", "/*") // cho phép tất cả các yêu cầu đối với các URL bắt đầu bằng /auth, /uploads, /products
                                .permitAll()
                                .requestMatchers("/admin/**").hasAuthority("ADMIN")  // yêu cầu người dùng có quyền "ADMIN" để truy cập các URL bắt đầu bằng /admin
                                .requestMatchers("/user/**").hasAuthority("USER")
                                .anyRequest().authenticated()  // yêu cầu xác thực cho tất cả các yêu cầu khác
                ).exceptionHandling(  // cấu hình xử lý exception liên quan đến bảo mật
                        (auth) -> auth
                                .authenticationEntryPoint(jwtEntryPoint)  // xác định jwtEntryPoint là điểm nhập cảnh cho xác thực không thành công
                                .accessDeniedHandler(accessDenied)  // xác định accessDenied là trình xử lý truy cập bị từ chối
                ).addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)  // sử dụng để kiểm tra và xác thực token JWT trong các yêu cầu
                .build();  // kết thúc cấu hình và trả về một SecurityFilterChain đã được cấu hình
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();  // xác thực người dùng từ một nguồn dữ liệu như cơ sở dữ liệu
        authenticationProvider.setUserDetailsService(userDetailService); // để tìm kiếm thông tin người dùng, bao gồm tên người dùng, mật khẩu và quyền hạn
        authenticationProvider.setPasswordEncoder(passwordEncoder());  // để mã hóa mật khẩu của người dùng để lưu trữ an toàn
        return authenticationProvider; // trả về authenticationProvider đã được cấu hình
    }
}
