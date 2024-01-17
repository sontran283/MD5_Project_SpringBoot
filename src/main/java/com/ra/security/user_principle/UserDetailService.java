package com.ra.security.user_principle;

import com.ra.model.entity.User;
import com.ra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    // Phương thức này được override từ UserDetailsService. Nó được gọi khi Spring Security cần thông tin chi tiết về người dùng để xác thực.
    // Sử dụng userRepository để lấy thông tin người dùng từ cơ sở dữ liệu dựa trên tên đăng nhập (username).
    // Tạo một đối tượng UserPrinciple (implements UserDetails) để lưu trữ thông tin về người dùng.
    // Gán danh sách quyền (roles) của người dùng vào authorities của UserPrinciple.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        UserPrinciple userPrinciple = new UserPrinciple();
        userPrinciple.setUser(user);
        userPrinciple.setAuthorities(user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList());
        return userPrinciple;
    }


    // Phương thức này nhận Authentication từ Spring Security và trích xuất userId từ đối tượng UserPrinciple.
    // Nếu Authentication không tồn tại hoặc đối tượng người dùng không phải là UserPrinciple, nó trả về null.
    // Nếu Authentication và đối tượng người dùng là UserPrinciple, nó trả về userId.
    public Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrinciple) {
            return ((UserPrinciple) principal).getUserId();
        }
        return null;
    }
}