package boot.utils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
public class JWTUtils {

    private static final String SECRET_KEY = "secret_key";

    public String JwtTokenGenerator (long userId){

            // 当前时间
            Date now = new Date();

            // 过期时间，设置为5小时后
            Date expiration = new Date(now.getTime() + 5 * 60 * 60 * 1000);

            // 创建JWT
            String jwt = Jwts.builder()
                    .setSubject(Long.toString(userId))  // 设置用户ID作为JWT的主题
                    .setIssuedAt(now)  // 设置JWT的发行时间
                    .setExpiration(expiration)  // 设置JWT的过期时间
                    .signWith(SignatureAlgorithm.HS256,SECRET_KEY)  // 使用密钥签名JWT
                    .compact();

            return jwt;

    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String userName) {
        String username = getUsernameFromToken(token);
        return username.equals(userName);
    }

}
