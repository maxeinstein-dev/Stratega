package br.com.maxsueleinstein.stratega.infrastructure.security;

import br.com.maxsueleinstein.stratega.domain.model.User;
import br.com.maxsueleinstein.stratega.domain.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class DemoAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${demo.auth.enabled:false}")
    private boolean demoAuthEnabled;

    @Value("${demo.user.email:demo.reviewer@stratega.dev}")
    private String demoUserEmail;

    public DemoAuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (shouldAuthenticateAsDemo(request)) {
            User demoUser = findOrCreateDemoUser();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    demoUser, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldAuthenticateAsDemo(HttpServletRequest request) {
        return demoAuthEnabled
                && request.getRequestURI().startsWith("/api/")
                && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private User findOrCreateDemoUser() {
        return userRepository.findByEmail(demoUserEmail)
                .orElseGet(() -> userRepository.save(new User(
                        null,
                        "Stratega Demo Reviewer",
                        demoUserEmail,
                        passwordEncoder.encode("demo-reviewer-password")
                )));
    }
}
