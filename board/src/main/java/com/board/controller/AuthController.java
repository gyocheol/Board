package com.board.controller;

import com.board.dto.SignUpDtoReq;
import com.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;

    /**
     * 회원가입
     * @param response
     * @param dto
     * @return OK
     */
    @PostMapping
    public ResponseEntity<HttpStatus> signUp(HttpServletResponse response, SignUpDtoReq dto) {
        memberService.signUp(response, dto);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
