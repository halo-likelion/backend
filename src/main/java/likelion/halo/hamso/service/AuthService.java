package likelion.halo.hamso.service;

import likelion.halo.hamso.configuration.EncodeConfig;
import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.dto.Member.MemberDto;
import likelion.halo.hamso.dto.Member.MemberJoinDto;
import likelion.halo.hamso.dto.Member.MemberLoginDto;
import likelion.halo.hamso.dto.Member.MemberUpdateAllDto;
import likelion.halo.hamso.exception.MemberDuplicateException;
import likelion.halo.hamso.exception.MemberNotFoundException;
import likelion.halo.hamso.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public String join(MemberJoinDto memberInfo){
        // loginID 중복 확인
        memberRepository.findByLoginId(memberInfo.getLoginId())
                .ifPresent(u -> {
                    throw new MemberDuplicateException("ID '" + memberInfo.getLoginId()+"' is already existed.");
                });

        Member member = Member.builder()
                .loginId(memberInfo.getLoginId())
                .password(encoder.encode(memberInfo.getPassword()))
                .email(memberInfo.getEmail())
                .name(memberInfo.getName())
                .phoneNo(memberInfo.getPhoneNo())
                .build();

        memberRepository.save(member);
        return member.getLoginId();
    }

    public Boolean checkDuplicate(String loginId){
        // loginID 중복 확인
        memberRepository.findByLoginId(loginId)
                .ifPresent(u -> {
                    throw new MemberDuplicateException(loginId+"is already existed.");
                });

        return false;
    }



    private static List<MemberDto> convertMemberToMemberDto(List<Member> memberList) {
        List<MemberDto> memberDtoList = memberList.stream()
                .map(a -> new MemberDto(a))
                .collect(Collectors.toList());
        return memberDtoList;
    }

    @Transactional
    public void updatePassword(MemberLoginDto memberInfo) {
        Optional<Member> oMember = memberRepository.findByLoginId(memberInfo.getLoginId());
        if(oMember.isEmpty()) {
            throw new MemberNotFoundException("Member not found with loginId: " + memberInfo.getLoginId());
        } else {
            oMember.get().setPassword(memberInfo.getPassword());
        }
    }
}
