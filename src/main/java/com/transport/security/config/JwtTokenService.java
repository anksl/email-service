package com.transport.security.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.transport.security.utils.SecurityConstants.AUTHORIZATION;
import static com.transport.security.utils.SecurityConstants.BEARER;
import static com.transport.security.utils.SecurityConstants.EMPTY;
import static com.transport.security.utils.SecurityConstants.EXPIRATION_TIME;
import static com.transport.security.utils.SecurityConstants.ROLES;
import static com.transport.security.utils.SecurityConstants.SECRET;

@Slf4j
@AllArgsConstructor
@Component
public class JwtTokenService {

    private final UserDetailsService userDetailsService;

    public Optional<Authentication> getAuthentication(HttpServletRequest request) {
        final String jwt = getJwtFromRequest(request);
        if (validateToken(jwt)) {
            return Optional.of(getAuthentication(new WebAuthenticationDetailsSource().buildDetails(request), jwt));
        }
        return Optional.empty();
    }

    private Authentication getAuthentication(WebAuthenticationDetails authDetails, String token) {
        final String username = getUsernameFromToken(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authentication.setDetails(authDetails);
        return authentication;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return (Claims) Jwts.parserBuilder().setSigningKey(SECRET).build().parse(token).getBody();
    }

    public String generateToken(Authentication authentication) {
        final Map<String, Object> claims = new HashMap<>();
        final UserDetails user = (UserDetails) authentication.getPrincipal();

        final List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put(ROLES, roles);
        return generateToken(claims, user.getUsername());
    }

    private String generateToken(Map<String, Object> claims, String subject) {
        final long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION_TIME))
                .signWith(new SecretKeySpec(Base64.getDecoder().decode(SECRET), SignatureAlgorithm.HS256.getJcaName()))
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET).build().parse(token).getBody();
        } catch (IllegalArgumentException | MalformedJwtException | ExpiredJwtException e) {
            log.error("Unable to get JWT Token or JWT Token has expired");
            return false;
        }
        return true;
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return EMPTY;
    }

}