package com.dulanjali.kitchen.securityConfig;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.dulanjali.kitchen.dao.UserDao;
import com.dulanjali.kitchen.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserDao userDao;

    public String getCurrentUserId() {
        String email = getCurrentUserEmail();
        return userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found for email: " + email))
                .getId();
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return principal.toString();
    }
}
