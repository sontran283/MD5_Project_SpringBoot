package com.ra.security.user_principle;

import com.ra.model.entity.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserPrinciple implements UserDetails {
    private User user;  // Đối tượng User chứa thông tin chi tiết về người dùng.

    private Collection<? extends GrantedAuthority> authorities; // authorities: Danh sách các quyền được cấp phép cho người dùng.

    public Long getUserId() {
        return user.getId();
    }  // Phương thức trả về userId từ đối tượng User.

    @Override  // getAuthorities: Trả về danh sách các quyền được cấp phép cho người dùng.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override // getPassword: Trả về mật khẩu của người dùng.
    public String getPassword() {
        return user.getPassword();
    }

    @Override // getUsername: Trả về tên đăng nhập của người dùng.
    public String getUsername() {
        return user.getUserName();
    }


    // Trả về giá trị true cho tất cả các phương thức này, đánh dấu rằng tài khoản không hết hạn,
    // không bị khóa, thông tin xác thực không hết hạn, và tài khoản được bật.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}