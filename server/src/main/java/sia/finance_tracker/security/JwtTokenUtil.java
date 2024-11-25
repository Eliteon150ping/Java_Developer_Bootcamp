package sia.finance_tracker.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final String SECRET_KEY = "jd1TyZ1XPgBmKG83ol7pHnLtAZHDtOPSbQTqRxjuUxE=";
    private static final long EXPIRATION_TIME = 100L * 365_25 * 24 * 60 * 60 * 1000;

//    public static String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }

    // Method to generate a token with userId and username
    public static String generateTokenWithUserId(Long userId, String username) {
        return Jwts.builder()
                .setSubject(username)  // Set the username as the subject of the token
                .claim("userId", userId)  // Add userId as a custom claim
                .setIssuedAt(new Date())  // Set the issue date
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // Set expiration time (1 hour)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // Sign with HS256 and secret key
                .compact();
    }

    public static String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Method to extract userId from token
//    public static Long extractUserId(String token) {
//        return Long.valueOf(Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody()
//                .get("userId", String.class));
//    }

    public static boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    private static boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().before(new Date());
    }
}

