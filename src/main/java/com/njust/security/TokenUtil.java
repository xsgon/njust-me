package com.njust.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.njust.model.user.Role;
import com.njust.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;

import javax.servlet.http.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenUtil {
    private static final long VALIDITY_TIME_MS = 2 * 60 * 60 * 1000; // 2 hours  validity
    private static final String AUTH_HEADER_NAME = "Authorization";

    private static final LoadingCache<String, String> logoutTokens = CacheBuilder.newBuilder()
        .expireAfterWrite(2, TimeUnit.HOURS)
        .maximumSize(102400)
        .build(
            new CacheLoader<String, String>() {
                public String load(String key) throws RuntimeException {
                    //return createExpensiveGraph(key);
                    throw new RuntimeException("not found");
                }
            });


    private String secret = "77T6?$+7x52a[c;t22~nHG62:hz6926Y";

    public void revokeToken(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null && !token.isEmpty()) {

            logoutTokens.put(token, "invalid");
//            log.info(token + " is logged out " + isTokenLoggedOut(token));
        }
    }

    private boolean isTokenLoggedOut(String token) {
        try {
//            log.info(token + " is " + logoutTokens.get(token));
            if (logoutTokens.get(token) != null) {
                return true;
            }
        } catch (Exception e) {
            ;
        }
        return false;
    }

    public Optional<Authentication> verifyToken(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);

        if (token != null && !token.isEmpty()) {
            if (isTokenLoggedOut(token)) {
                throw new JwtException("logged out already");
            }

            final TokenUser user = parseUserFromToken(token.replace("Bearer", "").trim());
            if (user != null) {
                return Optional.of(new UserAuthentication(user));
            }
        }
        return Optional.empty();

    }

    //Get UserMapper Info from the Token
    public TokenUser parseUserFromToken(String token) {

        Claims claims = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();

        UserVo user = new UserVo();
        user.setId((String) claims.get("userId"));
        user.setRole(Role.valueOf((String) claims.get("role")));
        if (user.getId() != null && user.getRole() != null) {
            return new TokenUser(user);
        } else {
            return null;
        }
    }

    public String createTokenForUser(TokenUser tokenUser) {
        return createTokenForUser(tokenUser.getUser());
    }

    public String createTokenForUser(UserVo user) {
        return Jwts.builder()
            .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS))
//            .setSubject(user.getFullName())
            .claim("userId", user.getId())
            .claim("role", user.getRole().toString())
            .claim("active", user.getActive())
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

}
