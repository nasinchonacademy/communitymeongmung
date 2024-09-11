package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.entity.Vet;
import org.zerock.projectmeongmung.repository.UserRepository;
import org.zerock.projectmeongmung.repository.VetRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
//스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final VetRepository vetRepository;


    public void updateUser(String uid, String nickname, String dogname, String profilePhoto, String dogbreed, Date dogbirthday) {
        userRepository.updateUser(uid, nickname, dogname, profilePhoto, dogbreed, dogbirthday);
    }


    //사용자 이름(id)로 사용자의 정보를 가져오는 메서드

    public User findUserByUid(String uid) {
        return userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + uid));
    }

    @Override
    public UserDetails loadUserByUsername(String uid) {
        // 사용자 정보를 UID로 찾음
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + uid));

        // 사용자가 관리자인 경우 ROLE_ADMIN, 그렇지 않으면 ROLE_USER 권한을 부여
        List<GrantedAuthority> authorities;
        if (user.isAdmin()) {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // Spring Security의 UserDetails 객체를 반환
        return new org.springframework.security.core.userdetails.User(
                user.getUid(),
                user.getPassword(),
                user.isActive(), // 계정 활성화 상태를 설정
                true, // 계정 만료 여부 (true = 만료되지 않음)
                true, // 비밀번호 만료 여부 (true = 만료되지 않음)
                true, // 계정 잠금 여부 (true = 잠금되지 않음)
                authorities
        );
    }
}
