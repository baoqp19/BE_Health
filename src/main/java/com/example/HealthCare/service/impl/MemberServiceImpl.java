package com.example.HealthCare.service.impl;

import com.example.HealthCare.dto.PaginationDTO.ResultPaginationDTO;
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

    @Override
    public Member addMember(Member member) {
        return this.memberRepository.save(member);
    }

    @Override
    public Member updateMember(Member member) {

        Member checkMember = this.memberRepository.findById(member.getMemberID())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        member.setUserID(checkMember.getUserID());

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
    public Page<Member> getAllMembers(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size); // page is 0-based
        if (keyword != null && !keyword.isEmpty()) {
            return memberRepository.findByKeyword(keyword, pageable);
        }
        return memberRepository.findAll(pageable); // Use pageable for pagination
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

}
