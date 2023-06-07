package codekoi.apiserver.global.token;

import codekoi.apiserver.domain.user.dto.UserToken;
import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;
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

    public String createAccessToken(UserToken userToken) {
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

    public UserToken parseByAccessToken(String token) {
        final Claims claims = getClaims(accessTokenSecretKey, token);

        Long id = claims.get("id", Long.class);
        return new UserToken(id);
    }

    public void validateRefreshToken(String token) {
        getClaims(refreshTokenSecretKey, token);
    }

    private Claims getClaims(Key access, String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(access)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException | IllegalArgumentException | ExpiredJwtException e) {
            throw new InvalidValueException(ErrorInfo.TOKEN_EXPIRED);
        }
    }
}
