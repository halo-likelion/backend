package likelion.halo.hamso.service;

import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.dto.Member.MemberDto;
import likelion.halo.hamso.dto.Member.MemberLoginDto;
import likelion.halo.hamso.dto.Member.MemberUpdateAllDto;
import likelion.halo.hamso.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member){
        memberRepository.save(member);
        return member.getId();
    }

    public MemberDto findById(Long id){
        Member member = memberRepository.findById(id).get();
        MemberDto memberDto = new MemberDto(member);
        return memberDto;
    }

    public MemberDto findByLoginId(String loginId){
        Member member = memberRepository.findByLoginId(loginId);
        MemberDto memberDto = new MemberDto(member);
        return memberDto;
    }

    public List<MemberDto> findAll() {
        List<Member> all = memberRepository.findAll();
        List<MemberDto> memberDtoList = convertMemberToMemberDto(all);
        return memberDtoList;
    }

    @Transactional
    public void updateMemberAll(MemberUpdateAllDto memberInfo) {
        Member member = memberRepository.findByLoginId(memberInfo.getLoginId());
        member.setName(member.getName());
        member.setEmail(member.getEmail());
        member.setPhoneNo(member.getPhoneNo());
    }

    @Transactional
    public void updatePassword(MemberLoginDto loginInfo) {
        Member member = memberRepository.findByLoginId(loginInfo.getLoginId());
        member.setPassword(loginInfo.getPassword());
    }

    @Transactional
    public void deleteMember(String loginId) {
        Member member = memberRepository.findByLoginId(loginId);
        memberRepository.delete(member);
    }

    private static List<MemberDto> convertMemberToMemberDto(List<Member> memberList) {
        List<MemberDto> memberDtoList = memberList.stream()
                .map(a -> new MemberDto(a))
                .collect(Collectors.toList());
        return memberDtoList;
    }
}
