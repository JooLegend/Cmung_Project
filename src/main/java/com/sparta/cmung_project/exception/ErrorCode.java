package com.sparta.cmung_project.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NotFoundMember(HttpStatus.NOT_FOUND.value(), "M001", "유저가 존재하지 않습니다."),
    DuplicatedUserId(HttpStatus.BAD_REQUEST.value(), "M002", "아이디를 사용하는 유저가 존재합니다"),
    WrongPassword(HttpStatus.BAD_REQUEST.value (), "M003", "비밀번호가 틀렸습니다."),

    // post
    MustHaveTitle(HttpStatus.BAD_REQUEST.value (), "P001", "제목은 반드시 입력해야 합니다."),
    NotFoundPost(HttpStatus.NOT_FOUND.value (), "P002", "게시물이 존재하지 않습니다."),
    NoPermissionToChange(HttpStatus.BAD_REQUEST.value (), "P003", "자신이 작성한 포스트만 수정가능합니다."),
    NoPermissionToDelete(HttpStatus.BAD_REQUEST.value (), "P004", "자신이 작성한 포스트만 삭제가능합니다.");

    // 필요하신 에러코드 더 작성해주세요!

    private final int status;
    private final String code;
    private final String message;
}