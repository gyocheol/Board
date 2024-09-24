package com.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final long REFRESH_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60; // 30일

    // RefreshToken 저장
    public void saveRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue().set(username, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    // RefreshToken 가져오기
    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(username);
    }

    // RefreshToken 삭제
    public void deleteRefreshToken(String username) {
        redisTemplate.delete(username);
    }
}