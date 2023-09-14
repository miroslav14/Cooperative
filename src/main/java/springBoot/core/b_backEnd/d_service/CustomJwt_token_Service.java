package springBoot.core.b_backEnd.d_service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Setter
@Getter
@Service
public class CustomJwt_token_Service {

    @Value("${jwt.key.secret}")
    private String SECRET_KEY;

    @Value("${jwt.key.timePeriodOfValidToken}")
    private String timePeriodOfValidToken;

    public String extractUsername(String token){
        return process_extractUsername(token);
    }

    private String process_extractUsername(String token) {
        return process_extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        return process_extractClaim(token, claimsResolver);
    }

    private <T> T process_extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extrctAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //generating token
    public String generateToken(Map<String, Objects> mapExtractClaims, UserDetails userDetails){
        return process_generateToken(mapExtractClaims,userDetails);
    }

    private String process_generateToken(Map<String, Objects> mapExtractClaims, UserDetails userDetails) {

        Long long_timePeriodOfValidToken = 0L;

        try{
            long_timePeriodOfValidToken = (Long.parseLong(timePeriodOfValidToken) * (1000 * 60));
            if(long_timePeriodOfValidToken < (1000 * 60)){
                throw new Exception("User did not specified correct time for token validity period so it will be set to default to 5 minutes");
            }
        }catch (Exception e){
            long_timePeriodOfValidToken += 1000 * 60 * 5;
        }

        return Jwts
                .builder()
                .setClaims(mapExtractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + long_timePeriodOfValidToken))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken_withUserDetailsOnly(UserDetails userDetails){
        return process_generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        return process_isTokenValid(token,userDetails);
    }

    private boolean process_isTokenValid(String token, UserDetails userDetails) {
        final String username = process_extractUsername(token);
        return ((username.equals(userDetails.getUsername())) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return process_isTokenExpired(token);
    }

    private boolean process_isTokenExpired(String token) {

        Date extractedDatefromToken = process_extractClaim(token,Claims::getExpiration);

        return extractedDatefromToken.before(new Date());
    }

    private Claims extrctAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }


}