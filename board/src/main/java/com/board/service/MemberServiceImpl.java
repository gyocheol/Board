package com.board.service;

import com.board.Repository.MemberRepository;
import com.board.dto.LoginDtoReq;
import com.board.dto.SignUpDtoReq;
import com.board.entity.Member;
import com.board.util.CustomException;
import com.board.util.JwtRequestFilter;
import com.board.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Override
    public void signUp(HttpServletResponse response, SignUpDtoReq dto) {
        // ID 중복 체크
        checkIdDuplicate(dto.getIdentity());

        Member member = Member.builder()
                .identity(dto.getIdentity())
                .password(dto.getPassword())
                .name(dto.getName())
                .build();

        // 비밀번호 일치 체크
        if (!dto.getPassword().equals(dto.getCheckPassword())){
            throw new CustomException("비밀번호가 일치하지 않습니다.");  // BAD_REQUEST(400)
        }
        // password 인코딩
        member.encodePassword(passwordEncoder);
        // member 저장
        memberRepository.save(member);

        // JWT 발급
        String accessToken = jwtUtil.generateAccessToken(member.getIdentity());
        String refreshToken = jwtUtil.generateRefreshToken(member.getIdentity());

        // RefreshToken을 Redis에 저장 / identity를 key로 사용
        redisService.saveRefreshToken(member.getIdentity(), refreshToken);

        // HTTPOnly 쿠키에 AccessToken 저장
        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(24*60*60);       // 1 day
        response.addCookie(accessCookie);

        // HTTPOnly 쿠키에 RefreshToken 저장
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(24*60*60*30);       // 30 day
        response.addCookie(refreshCookie);

        log.info("회원가입 완료!");
    }

    @Override
    public String login(LoginDtoReq dto) {
        return null;
    }


    private void checkIdDuplicate(String identity) {
        if (memberRepository.findByIdentity(identity).isPresent()) {
            throw new CustomException("중복된 아이디입니다.");
        }
    }
}
