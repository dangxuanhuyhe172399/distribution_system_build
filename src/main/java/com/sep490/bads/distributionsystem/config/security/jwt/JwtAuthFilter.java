package com.sep490.bads.distributionsystem.config.security.jwt;

import com.sep490.bads.distributionsystem.config.security.service.UserSecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


public class JwtAuthFilter extends OncePerRequestFilter {
    private static final List<String> PERMIT_ALL_PATHS = List.of(
            "/v1/public/auth", "/v1/doc", "/actuator", "/error", "/zalo_verifier"
    );

    private final JwtService jwtService;
    private final UserSecurityService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserSecurityService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    private boolean isPermitAll(HttpServletRequest request) {
        String p = request.getServletPath();
        if (p.startsWith("/v1/api-docs")) return true;
        return PERMIT_ALL_PATHS.stream().anyMatch(p::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (isPermitAll(request)) {
            chain.doFilter(request, response);
            return;
        }

        String auth = request.getHeader("Authorization");
        String token = null;
        if (auth != null) {
            int sp = auth.indexOf(' ');
            if (sp > 0 && "Bearer".equalsIgnoreCase(auth.substring(0, sp))) {
                token = auth.substring(sp + 1);
            }
        }

        try {
            if (token != null
                    && jwtService.validateToken(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                String subject = jwtService.extractSubject(token); // có thể là userId hoặc username
                if (subject != null && !subject.isBlank()) {
                    UserDetails user = userDetailsService.loadUserByUsername(subject);
                    var authToken = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } // nếu subject null → bỏ qua, để trả 401 thay vì 500
            }
        } catch (RuntimeException ex) {
            logger.debug("JWT auth failed: {}");
        }

        chain.doFilter(request, response);
    }
}
