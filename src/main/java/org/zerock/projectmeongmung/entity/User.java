package org.zerock.projectmeongmung.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Table(name="member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "uid", updatable = false, unique = true ,nullable = false)
    private String uid;

    @Column(name = "nickname" ,unique = true, nullable = false)
    private String nickname;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "password" ,nullable = false)
    private String password;

    @Column(name = "name" ,nullable = false)
    private String name;

    @Column(name = "dogname")
    private String dogname;

    @Column(name="dogbreed")
    private String dogbreed;

    @Column(name="Profilephoto")
    private String profilePhoto;


    @Column(name="dogbirthday")
    private Date dogbirthday;

    @Column(name = "dogmeeting")
    private String dogmeeting;

    @Column(name = "marketsns")
    private boolean marketsns;

    @Column(name = "locservice")
    private boolean locservice;

    @Column(name = "termuse")
    private boolean termuse;

    @Column(name = "personalinfo")
    private boolean personalinfo;

    @Column(name = "jellypoint", nullable = false)
    @ColumnDefault("0")
    private int jellypoint;

    @Column(name = "role", nullable = false)
    private boolean admin = false;

    @Column(name = "Active", nullable =false)
    private boolean active;

    @Column(name = "isvet", nullable = false)
    private boolean vet = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "regdate", nullable = false, updatable = false)
    @CreatedDate
    private Date regDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "withdrawaldate")
    private Date withdrawaldate;

    @OneToMany(mappedBy = "user")
    private Set<StoryLike> likes;

    @OneToMany(mappedBy = "user")
    private Set<GamePoints> gamePoints;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VetRecommendation> recommendations;

    @OneToMany(mappedBy = "user")
    private List<SOSboard> sosBoards;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<SOSboardcomment> comments;  // 유저가 작성한 댓글

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SOSboardlikecount> soslikes;  // SOSboardlikecount와의 관계

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Vet vetinfo;





    @Builder
    public User(String uid, String nickname, String email, String password, String name, String dogname, String dogbreed, String profilePhoto, Date dogbirthday,
                String dogmeeting , boolean marketsns, boolean locservice, boolean termuse,boolean personalinfo, int jellypoint, boolean admin, boolean vet, Vet vetinfo, boolean active) {
        this.uid = uid;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.name = name;
        this.dogname = dogname;
        this.dogbreed = dogbreed;
        this.profilePhoto = profilePhoto;
        this.dogbirthday = dogbirthday;
        this.dogmeeting = dogmeeting;
        this.marketsns = marketsns;
        this.locservice = locservice;
        this.termuse = termuse;
        this.jellypoint = jellypoint;
        this.personalinfo = personalinfo;
        this.regDate = new Date();
        this.admin = false;
        this.vet = vet;
        this.vetinfo=vetinfo;
        this.active = true;
    }


    //권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    //사용자의 id를 반환(고유한값)
    @Override
    public String getUsername() {
        return uid;
    }

    //사용자의 패스워드를 반환
    @Override
    public String getPassword() {
        return password;
    }

    //계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        //계정이 만료되었는지 확인하는 로직
        return UserDetails.super.isAccountNonExpired();
    }

    //계정의 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        //계정이 잠금되었는지 확인하는 로직
        return UserDetails.super.isAccountNonLocked();
    }

    //패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        //패스워드가 만료되었는지 확인 하는 로직
        return UserDetails.super.isCredentialsNonExpired();
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        // 계정이 사용가능한지 확인 하는 로직
        return UserDetails.super.isEnabled();
    }

    // 젤리 포인트를 조작할 수 있는 메서드
    // 추가
    public void addJellyPoints(int points) {
        this.jellypoint += points;
    }

    // 결제 시 감소
    public void subtractJellyPoints(int points) {
        if (this.jellypoint >= points) {
            this.jellypoint -= points;
        } else {
            throw new IllegalArgumentException("젤리가 부족해 추가 결제 금액이 발생합니다.");
        }
    }

}
