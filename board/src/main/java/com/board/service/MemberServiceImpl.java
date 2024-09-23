package com.board.service;

import com.board.Repository.MemberRepository;
import com.board.dto.LoginDtoReq;
import com.board.dto.SignUpDtoReq;
import com.board.entity.Member;
import com.board.util.CustomException;
import com.board.util.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtRequestFilter jwtRequestFilter;

    @Override
    public void signUp(SignUpDtoReq dto) {
        // TODO : ID 중복체크 어떻게 할지 생각하기 & 로그인이랑 회원가입 마무리
        Member member = Member.builder()
                .identity(dto.getIdentity())
                .password(dto.getPassword())
                .name(dto.getName())
                .build();

        // 비밀번호 일치 체크
        if (!dto.getPassword().equals(dto.getCheckPassword())){
            throw new CustomException("비밀번호가 일치하지 않습니다.");  // BAD_REQUEST(400)
        }
    }

    @Override
    public String login(LoginDtoReq dto) {
        return null;
    }

    @Override
    public boolean duplicatedId(String id) {
        return false;
    }
}
