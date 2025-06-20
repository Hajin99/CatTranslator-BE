package com.example.controller;


import com.example.apiPayload.ApiResponse;
import com.example.apiPayload.code.status.ErrorStatus;
import com.example.converter.MemberConverter;
import com.example.domain.entity.Member;
import com.example.domain.enums.Role;
import com.example.dto.MemberRequestDTO;
import com.example.dto.MemberResponseDTO;
import com.example.service.MemberService;
//import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.apiPayload.code.status.SuccessStatus.LOGIN_SUCCESS;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    //@Operation(summary = "회원가입 API", description = "회원가입하는 API입니다.")
    public ApiResponse<MemberResponseDTO.JoinResultDTO> join(@RequestBody @Valid MemberRequestDTO.JoinDto request){

        if (memberService.checkEmailExists(request.getEmail())) {
            // 이메일이 존재하면 에러
            return ApiResponse.onFailure(ErrorStatus.EMAIL_DUPLICATED);
        }
        else {
            // 기본적으로 USER 역할
            Role role = Role.USER;

            // 특정 이메일 주소인 경우 ADMIN 역할 추가
            if ("hajin@hansung.ac.kr".equals(request.getEmail())) {
                role = Role.ADMIN;
            }

            Member member = memberService.joinMember(request, role);
            return ApiResponse.onSuccess(MemberConverter.toJoinResultDTO(member));

        }

    }

    @PostMapping("/login")
    //@Operation(summary = "유저 로그인 API", description = "유저가 로그인하는 API입니다.")
    public ApiResponse<MemberResponseDTO.LoginResultDTO> login(@RequestBody @Valid MemberRequestDTO.LoginRequestDTO request) {
        return ApiResponse.of(LOGIN_SUCCESS, memberService.loginUser(request));
    }

    @GetMapping("/info")
    public ApiResponse<MemberResponseDTO.MemberInfoDTO> getMyInfo(HttpServletRequest request) {
        return ApiResponse.onSuccess(memberService.getMemberInfo(request));
    }
}
