package com.ddelight.ddAPI.common.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserUtil {

    public String getAuthenticatedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

}
