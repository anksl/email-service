package com.transport.security.impl;

import com.transport.api.exception.NoSuchEntityException;
import com.transport.api.exception.UniqueEntityException;
import com.transport.model.User;
import com.transport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) {
        List<User> users = userRepository.findByName(name);
        if (users.size() == 0)
            throw new NoSuchEntityException(String.format("User with name: %s doesn't exist", name));
        else if (users.size() > 1)
            throw new UniqueEntityException(String.format("More than one user with name: %s", name));
        User user = users.get(0);
        return new CustomUserPrincipal(user);
    }
}
