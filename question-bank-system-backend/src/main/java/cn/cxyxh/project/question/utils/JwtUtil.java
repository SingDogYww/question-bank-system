package cn.cxyxh.project.question.utils;

import cn.cxyxh.project.question.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import io.jsonwebtoken.*;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // 生成 Token（兼容 0.11.5 API）
    public String generateToken(User user) {
        byte[] bytes = Decoders.BASE64.decode(secret);
        SecretKey secretKey = Keys.hmacShaKeyFor(bytes);
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId()) // 添加自定义字段
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    // 从 Token 中提取用户ID（UUID）
    public String extractUserId(String token) {
        byte[] bytes = Decoders.BASE64.decode(secret);
        SecretKey secretKey = Keys.hmacShaKeyFor(bytes);
        return Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", String.class); // 获取自定义字段
    }

    // 验证 Token 有效性
    public boolean validateToken(String token) {
        try {
            byte[] bytes = Decoders.BASE64.decode(secret);
            SecretKey secretKey = Keys.hmacShaKeyFor(bytes);
            Jwts.parser().verifyWith(secretKey).build().parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}