package boardproject.sns.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Slf4j
public class JwtTokenUtils {

    private static String doGenerateToken(String userName, String key, long expireTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(getSigningKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims extractAllClaims(String token, String key) {
//        토큰에서 claims 추출하는 메소드
        Claims body = Jwts.parserBuilder()
                .setSigningKey(getSigningKey(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return body;
    }
    private static Key getSigningKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        return secretKey;
    }
    public static String generateAccessToken(String userName, String key, long expiredTimeMs) {
        return doGenerateToken(userName, key, expiredTimeMs);
    }
    //토큰이 만료 됐는지 검사
    public static Boolean isTokenExpired(String token, String key) {
        Date expiration = extractAllClaims(token, key).getExpiration();
        return expiration.before(new Date());
    }

    public static String getUsername(String token, String key) {
        return extractAllClaims(token, key).get("userName", String.class);
        //    Claims에서 이름 추출
    }


////    토큰 검사
//    public static Boolean validate(String token, String userName, String key) {
//        String userNameByToken = getUsername(token, key);
//        return userNameByToken.equals(userName) && !isTokenExpired(token, key);
//    }


}