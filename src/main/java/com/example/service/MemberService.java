package com.example.service;

import com.example.domain.entity.Member;
import com.example.domain.enums.Role;
import com.example.dto.MemberRequestDTO;
import com.example.dto.MemberResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    public Member joinMember(MemberRequestDTO.JoinDto request, Role role);
    MemberResponseDTO.LoginResultDTO loginUser(MemberRequestDTO.LoginRequestDTO request);

    boolean checkEmailExists(String email);
}
