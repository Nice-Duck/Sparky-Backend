package com.cmc.sparky.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // HTTP Status : 401 (Unauthorized)
    EXPIRE_TOKEN("U000", "토큰이 만료되었습니다."),
    INVALID_TOKEN("U001", "토큰이 유효하지않습니다."),

    // default HTTP Status : 404 (Not Found)
    INVALID_USER("F000", "회원 탈퇴한 이메일입니다."), //탈퇴 후 6개월 뒤 다시 회원가입 가능
    INVALID_TAG("F001", "태그가 존재하지 않습니다."),

    // default HTTP Status : 409 (Conflict)
    DUPLICATE_EMAIL("C001", "이메일이 중복되었습니다."),
    DUPLICATE_NICKNAME("C002", "닉네임이 중복되었습니다."),
    EXPIRE_NUMBER("C003", "인증 시간이 초과되었습니다."),
    INVALID_NUMBER("C004", "메일 인증 번호가 일치하지 않습니다."),
    INVALID_EMAIL("C005", "없는 이메일입니다."),
    INVALID_PASSWORD("C006", "비밀번호가 일치하지 않습니다."),
    DUPLICATE_TAG("C007", "이미 존재하는 태그입니다."),

    // HTTP Status : 500 (SERVER ERROR)
    NULL_DATABASE("S000", "return NULL"),
    INVALID_JJWT("S001", "JJWT SERVER ERROR");
    private String code;
    private String message;
    ErrorCode(String code, String message) {
        this.code=code;
        this.message=message;
    }
}
