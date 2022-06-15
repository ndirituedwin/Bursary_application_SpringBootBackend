package com.ndirituedwin.Security;

import com.ndirituedwin.Entity.Auth.User;
import com.ndirituedwin.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameoremail) throws UsernameNotFoundException {
        //let people login with their username or email;
        User user=userRepository.findByUsernameOrEmail(usernameoremail,usernameoremail)
                .orElseThrow(() -> new UsernameNotFoundException("User with the given username or email could not be found "+usernameoremail));

        log.info("logging the user credentials  {}",user);
        log.info("logging the user principal {}",UserPrincipal.class);
        return UserPrincipal.create(user);
    }
    //this method is used by jwtauthenticationFilter
    public UserDetails loadUserById(Long id){
        User user=userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("user not found with id "+id));
       log.info("loading user by id {}",user);
        return UserPrincipal.create(user);
    }
}
