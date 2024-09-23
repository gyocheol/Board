package com.board.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginDtoReq {
    @NotNull    // Controller 에 @Valid 를 넣어야 NotNull 어노테이션 활성화! NotNull은 Null만 허용하지 않음, "", " "는 허용
    private String identity;
    @NotNull
    private String password;
}
