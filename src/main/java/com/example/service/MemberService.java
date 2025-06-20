package com.example.service;

import com.example.domain.entity.Member;
import com.example.domain.enums.Role;
import com.example.dto.MemberRequestDTO;
import com.example.dto.MemberResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    public Member joinMember(MemberRequestDTO.JoinDto request, Role role);
    MemberResponseDTO.LoginResultDTO loginUser(MemberRequestDTO.LoginRequestDTO request);

    // 이메일 중복 체크
    boolean checkEmailExists(String email);

    // 마이페이지용 사용자 정보 조회
    MemberResponseDTO.MemberInfoDTO getMemberInfo(HttpServletRequest request);
}
