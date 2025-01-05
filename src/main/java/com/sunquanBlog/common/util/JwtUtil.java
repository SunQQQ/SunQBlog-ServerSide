package com.sunquanBlog.common.util;

import com.sunquanBlog.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "your_secret_key";

    // 生成 Token
    public static String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getName())
                .claim("id", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 验证 Token
    public static Claims validateToken(String token) throws Exception {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new Exception("Token 已过期", e);
        } catch (UnsupportedJwtException e) {
            throw new Exception("Token 格式不支持", e);
        } catch (MalformedJwtException e) {
            throw new Exception("Token 无效", e);
        } catch (SignatureException e) {
            throw new Exception("Token 签名无效", e);
        } catch (IllegalArgumentException e) {
            throw new Exception("Token 参数无效", e);
        }
    }
}
