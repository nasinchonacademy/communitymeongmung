package org.zerock.projectmeongmung.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vet implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vetid")
    private Long vetid;

    @Column(nullable = false)
    private String vetname;

    @Column(nullable = false)
    private String animalhospitlename;

    @Column(nullable = false)
    private String registerdate;

    @Column
    private Timestamp withdrawaldate; // 탈퇴 예정일

    @Column(length = 500)
    private String profilePhoto;  // 프로필 사진 컬럼 추가

    @Column(nullable = false)
    private Boolean visibility; // 공개 여부 컬럼 (Boolean 타입)

    @ElementCollection
    @CollectionTable(name = "vet_description", joinColumns = @JoinColumn(name = "vetid"))
    @Column(name = "description")
    private List<String> description = new ArrayList<>();  // 소개 필드

    @Column(nullable = false)
    @ColumnDefault("0")
    private int recommendationCount;

    @OneToMany(mappedBy = "vet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VetRecommendation> recommendations;

    // 로그인 관련 필드 추가
    @Column(nullable = false, unique = true)
    private String username; // 사용자 ID

    @Column(nullable = false)
    private String password; // 비밀번호 (암호화된 상태로 저장)

    @Column(nullable = false, unique = true)
    private String email;  // 수의사 이메일 추가

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "userid", nullable = false)
    @JsonIgnore
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_VET")); // 수의사 역할 부여
    }

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
        return true; // 추가적인 조건으로 비활성화 처리 가능
    }




    public User getUser() {
        return this.user;  // User 필드를 반환
    }

    @Transient
    public int getCommentCount() {
        if (this.user != null && this.user.getComments() != null) {
            return this.user.getComments().size(); // 해당 수의사와 연결된 사용자가 작성한 댓글 수
        }
        return 0;
    }


}
;


