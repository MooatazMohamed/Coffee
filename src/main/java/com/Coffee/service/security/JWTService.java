package com.Coffee.service.security;

import com.Coffee.model.Branch;
import com.Coffee.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JWTService {

    private String secretKey;

    public JWTService(){
        secretKey=generateSecretKey();
    }

    public String generateSecretKey(){
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGenerator.generateKey();
            System.out.println("Secret Key"+secretKey.toString());
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public String generateToken(Long id, String name, String email, Role role , Long branchId){
        Map<String,Object> claims = new HashMap<>();

        claims.put("id", id);
        claims.put("name", name);
        claims.put("email", email);
        claims.put("role",role.getName());
        // Only add branch_id if role is MANAGER and branchId is not null
        if(role.getName().equals("MANAGER") && branchId != null){
            claims.put("branch_id", branchId);
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*30))
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    public Long extractBranchId(String token) {
        Claims claims = extractAllClaims(token);
        System.out.println(claims);
        return ((Number) claims.get("branch_id")).longValue();
    }
    public Long extractId(String token){
        return extractClaim(token, claims -> claims.get("id", Long.class));
    }

    public String extractEmail(String token){
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public String extractName(String token){
        return extractClaim(token, claims -> claims.get("name", String.class));
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        token = token.replace("Bearer ", "").trim();
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
