package top.amfun.simple.security.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2020/10/26 17:58
 * @description: JwtToken生成的工具类
 * JWT token的格式：header.payload.signature
 * header的格式（算法、token的类型）：
 * {"alg": "HS512","typ": "JWT"}
 * payload的格式（用户名、创建时间、生成时间）：
 * {"sub":"wang","created":1489079981393,"exp":1489684781}
 * signature的生成算法：
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 */
public class JwtTokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREADED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取的Jwt的负载
     * @param token
     * @return
     */
    private Claims getClaimsFormToken(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.info("JWT格式校验失败：{}",token);
        }
        return claims;
    }
    private Date generateExpirationDate(){
        return new Date(System.currentTimeMillis() + expiration*1000);
    }

    /**
     * 从token中获取登录用户名
     * @param token
     * @return
     */
    public String getUsernameFormToken(String token){
        String username;
        try {
            Claims claims = getClaimsFormToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
            LOGGER.info("用户名获取失败：{}",token);
        }
        return username;
    }

    /**
     * 校验token是否还有效
     * @param token
     * @param userDetails 从数据库中查询出用户信息
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails){
        String username = getUsernameFormToken(token);
        return !isTokenExired(token) && username.equals(userDetails.getUsername());
    }

    /**
     * token是否已经失效
     * @param token
     * @return
     */
    private boolean isTokenExired(String token) {
        Date date = getExpiredDateFormToken(token);
        return date.before(new Date());
    }
    private Date getExpiredDateFormToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREADED, new Date());
        return generateToken(claims);
    }

    /**
     * 当原token未过期时可以刷新
     * @param oldToken
     * @return
     */
    public String refreshHeadToken(String oldToken) {
        if (StrUtil.isEmpty(oldToken)) {
            return null;
        }
        String token = oldToken.substring(tokenHead.length());
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        // 校验token
        Claims claims = getClaimsFormToken(token);
        if (claims==null) {
            return null;
        }
        // token过期
        if (isTokenExired(token)) {
            return null;
        }
        if (refreshTokenJustBefore(token,30*60)) {
            return token;
        } else {
            claims.put(CLAIM_KEY_CREADED,new Date());
            return generateToken(claims);
        }
    }

    /**
     * 判断指定时间内刚刷新过
     * @param token
     * @param time 指定时间范围
     * @return
     */
    private boolean refreshTokenJustBefore(String token,int time) {
        Claims claims = getClaimsFormToken(token);
        Date createdTokenDate = claims.get(CLAIM_KEY_CREADED, Date.class);
        Date refreshDate = new Date();
        if (refreshDate.after(createdTokenDate) &&
                refreshDate.before(DateUtil.offsetSecond(createdTokenDate,time))) {
            return true;
        } else {
            return false;
        }
    }
}
