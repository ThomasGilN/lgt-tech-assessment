package com.tgn.itknowledgebase.infraestructure.adapter.in.auth;

import com.tgn.itknowledgebase.infraestructure.port.in.auth.GetCurrentAuthenticatedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class SpringSecurityGetCurrentAuthenticatedUserAdapter implements GetCurrentAuthenticatedUser
{
    @Override
    public String getUsername() {
        var authentication = (JwtAuthenticationToken) SecurityContextHolder
                .getContext()
                .getAuthentication();
        return authentication.getToken().getSubject();
    }
}
