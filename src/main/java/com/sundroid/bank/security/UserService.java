package com.sundroid.bank.security;

import com.sundroid.bank.appuser.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<AppUser> user = userRepository.findByEmail(s.toLowerCase());


        if (user.isPresent()){
            return user.get();
        }
        else{
            throw new UsernameNotFoundException(String.format("Username[%s] not found"));
        }
    }

    public AppUser saveUser(AppUser s){
        String encodedPassword = bCryptPasswordEncoder
                .encode(s.getPassword());

        s.setPassword(encodedPassword);
        s.setEmail(s.getEmail().toLowerCase());
        AppUser user = userRepository.save(s);
        return  user;
    }

    public boolean isUserExist(String email){
        boolean userExists = userRepository
                .findByEmail(email)
                .isPresent();
        return userExists;
    }

    public AppUser getUser(String s){
        Optional<AppUser> user = userRepository.findByEmail(s);
        return user.orElseThrow();
    }
    public AppUser updateUser(AppUser s){
        s.setEmail(s.getEmail().toLowerCase());
        AppUser user = userRepository.save(s);
        return  user;
    }
}
