package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.UserRepository;

import java.util.Date;

@RequiredArgsConstructor
@Service
//스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;


    public void updateUser(String uid, String nickname, String dogname, String profilePhoto, String dogbreed, Date dogbirthday) {
        userRepository.updateUser(uid, nickname, dogname, profilePhoto, dogbreed, dogbirthday);
    }


    //사용자 이름(id)로 사용자의 정보를 가져오는 메서드
    @Override
    public User loadUserByUsername(String uid) {
        return userRepository.findByUid(uid)
                .orElseThrow(()-> new IllegalArgumentException(uid));
    }
}
