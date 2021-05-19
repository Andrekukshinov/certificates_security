package com.epam.esm.web.filter;

import com.epam.esm.web.security.jwt.JwtManager;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtManager jwtManager;


    public JwtVerificationFilter(JwtManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorization == null || !authorization.startsWith("Jwt ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String jwt = authorization.replace("Jwt ", "");
            String subject = jwtManager.getUsername(jwt);
            if (!jwtManager.isNotExpiredToken(jwt)) {
                throw new InsufficientAuthenticationException("token expired!");
            }  else {
                Set<? extends GrantedAuthority> grantedAuthorities = jwtManager.getAuthorities(jwt);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        subject,
                        null,
                        grantedAuthorities
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            }
        } catch (JwtException e) {
            throw new InsufficientAuthenticationException(e.getMessage());
        }
    }
}
