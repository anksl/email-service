package com.transport.service;

import com.transport.api.dto.AuthenticationDto;

public interface AuthenticationService {
    String createAuthenticationToken(AuthenticationDto authenticationRequest);
}
