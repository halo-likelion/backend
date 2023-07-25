package likelion.halo.hamso.service;

import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.dto.Member.MemberDto;
import likelion.halo.hamso.dto.Member.MemberLoginDto;
import likelion.halo.hamso.dto.Member.MemberUpdateAllDto;
import likelion.halo.hamso.exception.MemberNotFoundException;
import likelion.halo.hamso.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDto findById(Long id){
        Member member = memberRepository.findById(id).get();
        MemberDto memberDto = new MemberDto(member);
        return memberDto;
    }

    public MemberDto findByLoginId(String loginId){
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if(member.isEmpty()) {
            // Member not found, handle accordingly
            throw new MemberNotFoundException("Member not found with loginId: " + loginId);
        } else {
            return new MemberDto(member.get());
        }
    }

    public List<MemberDto> findAll() {
        List<Member> all = memberRepository.findAll();
        List<MemberDto> memberDtoList = convertMemberToMemberDto(all);
        return memberDtoList;
    }

    @Transactional
    public void updateMemberAll(MemberUpdateAllDto memberInfo) {
        Optional<Member> oMember = memberRepository.findByLoginId(memberInfo.getLoginId());
        if(oMember.isEmpty()) {
            // Member not found, handle accordingly
            throw new MemberNotFoundException("Member not found with loginId: " + memberInfo.getLoginId());
        } else {
            Member member = oMember.get();
            member.setName(member.getName());
            member.setEmail(member.getEmail());
            member.setPhoneNo(member.getPhoneNo());
        }

    }

    @Transactional
    public void deleteMember(String loginId) {
        Optional<Member> oMember = memberRepository.findByLoginId(loginId);
        if(oMember.isEmpty()) {
            // Member not found, handle accordingly
            throw new MemberNotFoundException("Member not found with loginId: " + loginId);
        } else {
            Member member = oMember.get();
            memberRepository.delete(member);
        }
    }

    private static List<MemberDto> convertMemberToMemberDto(List<Member> memberList) {
        List<MemberDto> memberDtoList = memberList.stream()
                .map(a -> new MemberDto(a))
                .collect(Collectors.toList());
        return memberDtoList;
    }
}
