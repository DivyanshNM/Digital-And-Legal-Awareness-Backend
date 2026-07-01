package com.Legal.awareness.DigitalAwareness.security.config;

import com.Legal.awareness.DigitalAwareness.auth.service.JwtService;
import com.Legal.awareness.DigitalAwareness.user.entity.User;
import com.Legal.awareness.DigitalAwareness.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public  JwtFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            String json = """
                    {
                        "message": "JWT Token Not Provided",
                        "status": 401,
                        "timestamp": %d,
                        "path": "%s"
                    }
                    """.formatted(System.currentTimeMillis(), request.getRequestURI());

            response.getWriter().write(json);
            return;
        }else{
            try {
                String token = authHeader.substring(7);
                String userEmail = jwtService.extractUsername(token);

                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    User user = userRepository.findByEmail(userEmail)
                            .orElseThrow(() -> new RuntimeException("User Not Found"));

                    if (jwtService.isTokenValid(token, user)) {

                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        user,
                                        null,
                                        List.of(new SimpleGrantedAuthority(user.getRole().name()))
                                );

                        authenticationToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }

            } catch (Exception ex) {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");

                String json = """
                        {
                            "message": "Invalid or Expired JWT Token",
                            "status": 401,
                            "timestamp": %d,
                            "path": "%s"
                        }
                        """.formatted(System.currentTimeMillis(), request.getRequestURI());

                response.getWriter().write(json);
                return;
            }
            filterChain.doFilter(request, response);
        }

    }
}
