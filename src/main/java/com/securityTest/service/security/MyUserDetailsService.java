package com.securityTest.service.security;

import com.securityTest.model.User;
import com.securityTest.model.UserPrincipal;
import com.securityTest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null){
            System.out.println("404 NotFound");
            throw new UsernameNotFoundException("User 404");
        }
        return new UserPrincipal(user);
    }
}
