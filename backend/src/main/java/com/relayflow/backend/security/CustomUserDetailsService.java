package com.relayflow.backend.security;

import com.relayflow.backend.user.UserRpository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//charge user depuis la DB
//sql :email → SELECT user → return UserDetails
//java : repo.findByEmail(email)
@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRpository repo ;

  public CustomUserDetailsService(UserRpository repo) {
      this.repo = repo;
  }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

var user=repo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                .build();
    }
}
