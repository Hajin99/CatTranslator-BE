package com.example.apiPayload.code.status;

import com.example.apiPayload.code.BaseErrorCode;
import com.example.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),
    //일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    FOOD_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON400", "잘못된 요청입니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON400", "잘못된 요청입니다."),

    // 회원가입 에러
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "MEMBER4002", "이미 가입된 이메일입니다."),

    // 멤버 관려 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER4003", "패스워드가 불일치합니다."),

    // 페이징 에러
    PAGE_INVALID(HttpStatus.BAD_REQUEST, "PAGE001", "page는 1 이상의 값이어야 합니다."),

    // 인증 에러
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH001", "유효하지 않은 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason(){
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}