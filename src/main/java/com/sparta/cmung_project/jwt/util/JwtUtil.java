package com.sparta.cmung_project.jwt.util;

import com.sparta.cmung_project.jwt.dto.TokenDto;
import com.sparta.cmung_project.model.RefreshToken;
import com.sparta.cmung_project.repository.RefreshTokenRepository;
import com.sparta.cmung_project.security.user.UserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsServiceImpl userDetailsService;

    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";

    private static final long ACCESS_TIME = 24 * 3600 * 1000L; // 1일
    private static final long REFRESH_TIME = 7 * 24 * 3600 * 1000L; // 일주일


    @Value ( "${jwt.secret.key}" )
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder ().decode ( secretKey );
        key = Keys.hmacShaKeyFor ( bytes );
    }

    // header 토큰을 가져오는 기능
    public String getHeaderToken(HttpServletRequest request, String type) {
        return type.equals ( "Access" ) ? request.getHeader ( ACCESS_TOKEN ) : request.getHeader ( REFRESH_TOKEN );
    }

    // 토큰 생성
    public TokenDto createAllToken(String userId) {
        return new TokenDto ( createToken ( userId, "Access" ), createToken ( userId, "Refresh" ) );
    }

    public String createToken(String userId, String type) {

        Date date = new Date ();
        long time = type.equals ( "Access" ) ? ACCESS_TIME : REFRESH_TIME;

        return Jwts.builder ()
                .setSubject ( userId )
                .setExpiration ( new Date ( date.getTime () + time ) )
                .setIssuedAt ( date )
                .signWith ( key, signatureAlgorithm )
                .compact ();
    }

    // 토큰 검증
    public Boolean tokenValidation(String token) {
        try {
            Jwts.parserBuilder ().setSigningKey ( key ).build ().parseClaimsJws ( token );
            return true;
        } catch (Exception ex) {
            log.error ( ex.getMessage () );
            return false;
        }
    }

    // refreshToken 검증
    public Boolean refreshTokenValidation(String token) {

        // 1차 토큰 검증
        if(!tokenValidation ( token )) return false;

        // DB에 저장한 토큰 비교
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberUserId ( getUserIdFromToken ( token ) );

        return refreshToken.isPresent () && token.equals ( refreshToken.get ().getRefreshToken () );
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String userId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername (userId);

        return new UsernamePasswordAuthenticationToken ( userDetails, "", userDetails.getAuthorities () );
    }

    // 토큰에서 userId를 가져오는 기능
    public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder ().setSigningKey ( key ).build ().parseClaimsJws ( token ).getBody ().getSubject ();
    }
}
