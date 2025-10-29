package com.sep490.bads.distributionsystem.security.jwt;

import com.sep490.bads.distributionsystem.security.service.UserDetailsImpl;
import com.sep490.bads.distributionsystem.security.service.UserSecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// This class helps us to validate the generated jwt token
@Component
@Log4j2
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final List<String> PERMIT_ALL_PATHS = List.of("/v1/public");

    @Autowired @Lazy private JwtService jwtService;
    @Autowired @Lazy private UserSecurityService userDetailsService;

    private boolean isPermitAll(HttpServletRequest request) {
        String path = request.getServletPath();
        return PERMIT_ALL_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (isPermitAll(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.substring(7) : null;

        if (token != null && jwtService.validateToken(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            String username = jwtService.extractSubject(token); // subject = username
            UserDetailsImpl userDetails =
                    (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
