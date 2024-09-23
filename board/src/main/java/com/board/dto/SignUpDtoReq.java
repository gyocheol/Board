package com.board.dto;

import com.board.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class SignUpDtoReq {
    @NotBlank
    @Size(min = 4, max = 16)
    private String identity;

    @NotBlank
    @Size(min = 8, max = 16)    // gradle 에 validation 의존성 추가
    private String password;

    @NotBlank
    @Size(min = 8, max = 16)
    private String checkPassword;

    @NotBlank
    @Size(min = 2, max = 10)
    private String name;

    public SignUpDtoReq(Member member) {
        this.identity = member.getIdentity();
        this.password = member.getPassword();
        this.name = member.getName();
    }
}
