package likelion.halo.hamso.service;

import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByLoginId(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Member member) {
        // 만약 로그인 아이디가 gapyeong11이라면 ADMIN 권한을 부여
        if ("gapyeong11".equals(member.getLoginId())) {
            return User.builder()
                    .username(member.getUsername())
                    .password(member.getPassword())
                    .roles("ADMIN")  // role을 "ADMIN"으로 설정
                    .build();
        } else {
            // 그렇지 않다면 기존처럼 USER 권한을 부여
            return User.builder()
                    .username(member.getUsername())
                    .password(member.getPassword())
                    .roles("USER") // role을 "USER"로 설정
                    .build();
        }
    }
}