package com.gibanator.dailystepbackendjava.auth.security;

import com.gibanator.dailystepbackendjava.user.UserEntity;
import com.gibanator.dailystepbackendjava.user.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String token = authHeader.substring(7);

            FirebaseToken decodedToken = FirebaseAuth
                    .getInstance()
                    .verifyIdToken(token);

            String firebaseUid = decodedToken.getUid();
            String email = decodedToken.getEmail();

            UserEntity user = userRepository
                    .findByFirebaseUid(firebaseUid)
                    .orElseGet(() -> {
                        UserEntity newUser = new UserEntity();

                        newUser.setFirebaseUid(firebaseUid);
                        newUser.setEmail(email);
                        newUser.setCreatedAt(LocalDateTime.now());

                        return userRepository.save(newUser);
                    });

            UserPrincipal principal =
                    new UserPrincipal(
                            user.getId(),
                            user.getFirebaseUid(),
                            user.getEmail()
                    );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            Collections.emptyList()
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

        } catch (Exception e) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid Firebase token"
            );

            return;
        }

        filterChain.doFilter(request, response);
    }
}