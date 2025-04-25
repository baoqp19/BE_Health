package com.example.HealthCare.service.impl;

import com.example.HealthCare.dto.PaginationDTO.ResultPaginationDTO;
import com.example.HealthCare.mapper.MemberMapper;
import com.example.HealthCare.model.Member;
import com.example.HealthCare.repository.MemberRepository;
import com.example.HealthCare.service.MemberService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    @Override
    public Member addMember(Member member) {
        return this.memberRepository.save(member);
    }

    @Override
    public Member updateMember(Member member) {

        Member checkMember = this.memberRepository.findById(member.getMemberID())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        member.setUser(checkMember.getUser());

        return this.memberRepository.save(member);
    }

    @Override
    public void deleteMember(Integer memberID) {
        this.memberRepository.deleteById(memberID);
    }

    @Override
    public Member getMemberById(Integer memberID) {
        return this.memberRepository.findById(memberID)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    @Override
    public Page<Member> getAllMembers(int page, int size, String keyword, Integer userID) {
        Pageable pageable = PageRequest.of(page - 1, size); // page is 0-based
        if (keyword != null && !keyword.isEmpty()) {
            return this.memberRepository.findByKeyword(keyword, pageable, userID);
        }
        return this.memberRepository.findAllByUserID(pageable, userID); // Use pageable for pagination
    }

    @Override
    public ResultPaginationDTO getAllMember(Pageable pageable) {

        Page<Member> pageMember = this.memberRepository.findAll(pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();

        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageMember.getTotalPages());
        mt.setTotal(pageMember.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageMember.getContent());

        return rs;

    }
    @Override
    public List<Member> getAllMembersByUserID(Integer userID) {
        return memberRepository.findAllByUserID(userID);
    }

}
