package com.board.service;

import com.board.dto.LoginDtoReq;
import com.board.dto.SignUpDtoReq;

public interface MemberService {

    void signUp(SignUpDtoReq dto);
    String login(LoginDtoReq dto);
    boolean duplicatedId(String id);
}
