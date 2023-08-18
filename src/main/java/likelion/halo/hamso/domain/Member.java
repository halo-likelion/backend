package likelion.halo.hamso.domain;

import jakarta.persistence.*;
import likelion.halo.hamso.dto.member.MemberJoinDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter @ToString
@Table(name = "member")
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "login_id", unique = true)
    private String loginId; // 로그인 아이디

    @Column(name = "pwd")
    private String password; // 비밀번호

    @Column(name = "name")
    private String name; // 회원의 이름

    @Column(name = "phn")
    private String phoneNo; // 전화번호

    @Column(name="email")
    private String email; // 이메일

    @Column(name = "address")
    private String address; // 도로명주소

    @Column(name = "specific_address")
    private String specificAddress; // 상세주소

    public Member(MemberJoinDto memberInfo) {
        this.loginId = memberInfo.getLoginId();
        this.name = memberInfo.getName();
        this.phoneNo = memberInfo.getPhoneNo();
        this.email = memberInfo.getEmail();
        this.password = memberInfo.getPassword();
        this.address = memberInfo.getAddress();
        this.specificAddress = memberInfo.getSpecificAddress();
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return loginId;
    }
    @Override
    public String getPassword() {
        return password;
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
        return true;
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Reservation> reservationList;
}
