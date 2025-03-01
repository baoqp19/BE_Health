package com.example.HealthCare.service;

import com.example.HealthCare.dto.PaginationDTO.ResultPaginationDTO;
import com.example.HealthCare.model.Member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    Member addMember(Member member);

    Member updateMember(Member member);

    void deleteMember(Integer memberID);

    Member getMemberById(Integer memberID);

    Page<Member> getAllMembers(int page, int size, String keyword);

    public ResultPaginationDTO getAllMember(Pageable pageable);
}
