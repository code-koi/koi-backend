package com.codekoi.coreweb.jwt;

import com.codekoi.coreweb.jwt.exception.AccessTokenExpiredException;
import com.codekoi.coreweb.jwt.exception.InvalidTokenTypeException;
import com.codekoi.coreweb.jwt.exception.RefreshTokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private long accessTokenValidMilliseconds;
    private long refreshTokenValidMilliseconds;

    private Key refreshTokenSecretKey;
    private Key accessTokenSecretKey;

    public JwtTokenProvider(@Value("${security.jwt.expire-length.access-token}") long accessTokenValidMilliseconds,
                            @Value("${security.jwt.expire-length.refresh-token}") long refreshTokenValidMilliseconds,
                            @Value("${security.jwt.secret-key}") String accessTokenSecretKey,
                            @Value("${security.jwt.refresh-secret-key}") String refreshTokenSecretKey) {
        this.accessTokenValidMilliseconds = accessTokenValidMilliseconds;
        this.refreshTokenValidMilliseconds = refreshTokenValidMilliseconds;
        this.accessTokenSecretKey = Keys.hmacShaKeyFor(accessTokenSecretKey.getBytes(StandardCharsets.UTF_8));
        this.refreshTokenSecretKey = Keys.hmacShaKeyFor(refreshTokenSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(AuthInfo userToken) {
        final Date now = new Date();
        final Date tokenExpiredAt = new Date(now.getTime() + accessTokenValidMilliseconds);

        return Jwts.builder()
                .claim("id", userToken.getUserId())
                .setIssuedAt(now)
                .setExpiration(tokenExpiredAt)
                .signWith(accessTokenSecretKey)
                .compact();
    }

    public String createRefreshToken() {
        final Date now = new Date();
        final Date tokenExpiredAt = new Date(now.getTime() + refreshTokenValidMilliseconds);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(tokenExpiredAt)
                .signWith(refreshTokenSecretKey)
                .compact();
    }

    public AuthInfo parseAccessToken(String token) {
        try {
            return parse(token);
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException();
        }
    }

    public AuthInfo parseExpirableAccessToken(String token) {
        try {
            return parse(token);
        } catch (ExpiredJwtException e) {
            return getUserToken(e.getClaims());
        }
    }

    private AuthInfo parse(String token) {
        try {
            final Claims claim = getClaim(accessTokenSecretKey, token);
            return getUserToken(claim);
        } catch (MalformedJwtException | IllegalArgumentException e) {
            throw new InvalidTokenTypeException();
        }
    }

    public void validateExpiredRefreshToken(String token) {
        try {
            getClaim(refreshTokenSecretKey, token);
        } catch (MalformedJwtException | IllegalArgumentException exception) {
            throw new InvalidTokenTypeException();
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException();
        }
    }

    private Claims getClaim(Key key, String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private AuthInfo getUserToken(Claims claim) {
        final Long userId = claim.get("id", Long.class);
        return new AuthInfo(userId);
    }
}
