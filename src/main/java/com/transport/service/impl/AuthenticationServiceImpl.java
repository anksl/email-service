package com.transport.service.impl;

import com.transport.api.dto.AuthenticationDto;
import com.transport.security.config.JwtTokenService;
import com.transport.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenUtil;

    @Override
    @SneakyThrows
    public String createAuthenticationToken(AuthenticationDto authenticationRequest) {

        final Authentication auth = authenticate(authenticationRequest.getName(), authenticationRequest.getPassword());

        SecurityContextHolder.getContext().setAuthentication(auth);

        return jwtTokenUtil.generateToken(auth);
    }

    private Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
