package com.example.main.service;

import com.example.main.domain.Role;
import com.example.main.domain.UR;
import com.example.main.domain.User;
import com.example.main.repos.URRepo;
import com.example.main.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private URRepo urRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
    public boolean addUser(User user){
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return true;
    }
    public boolean deleteUser(User user)
    {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb == null) {
            return false;
        }
        UR ur = urRepo.findByUserID(userFromDb.getId());
        if (ur != null)
            urRepo.delete(ur);
        userRepo.delete(userFromDb);
        return true;
    }
}
