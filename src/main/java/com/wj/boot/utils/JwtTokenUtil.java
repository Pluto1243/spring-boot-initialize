package com.wj.boot.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT生成令牌、验证令牌、获取令牌
 *
 * @author wangjie
 * @date 12:09 2022年07月26日
 **/
@Component
public class JwtTokenUtil {
    /**
     * 私钥
     */
    private static final String SECRET_KEY = "my-boot";

    /**
     * 过期时间 毫秒,设置默认1天的时间过期
     */
    private  static final long EXPIRATION_TIME = 3600000L * 24;

    /**
     * 生成令牌
     *
     * @param token
     * @return 令牌
     */
    public String generateToken(String token) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(Claims.SUBJECT, token);
        claims.put(Claims.ISSUED_AT, new Date());

        return generateToken(claims);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);
            System.out.println("claims = " + claims.toString());
            username = claims.getSubject();
        } catch (Exception e) {
            System.out.println("e = " + e.getMessage());
        }
        return username;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) throws  Exception{
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            new Throwable(e);
        }
        return true;
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(Claims.ISSUED_AT, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 移除token
     * @param token
     */
    public void removeToken(String token) {
        try {
            // TODO 需要根据业务情况编写 如使用redis
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis()+ EXPIRATION_TIME);
        JwtBuilder jwtBuilder = Jwts.builder().setClaims(claims).setExpiration(expirationDate);
        JwtBuilder jwtBuilder1 = jwtBuilder.signWith(SignatureAlgorithm.HS512, SECRET_KEY);
        String compact = jwtBuilder1.compact();
        return compact;
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token 令牌
     * @return 用户Id
     */
    public Integer getIdFromToken(String token) {
        Integer id = null;
        try {
            Claims claims = getClaimsFromToken(token);
            System.out.println("claims = " + claims.toString());
            id = Integer.parseInt(claims.getId());
        } catch (Exception e) {
            System.out.println("e = " + e.getMessage());
        }
        return id;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) throws Exception {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            new Throwable(e);
        }
        return claims;
    }
}