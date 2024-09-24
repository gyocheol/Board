package com.board.service;

import com.board.dto.LoginDtoReq;
import com.board.dto.SignUpDtoReq;

import javax.servlet.http.HttpServletResponse;

public interface MemberService {

    void signUp(HttpServletResponse response, SignUpDtoReq dto);
    String login(LoginDtoReq dto);

}
