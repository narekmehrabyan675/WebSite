package com.example.demo.Services;

import com.example.demo.MySQL.Role;
import com.example.demo.MySQL.User;
import com.example.demo.repo.RoleRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MailSenderService mailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);
        return user;
    }

    public boolean addUser(User user){
        User userFromDB = userRepository.getUserByUsername(user.getUsername());
        if(userFromDB != null){
            return false;
        }
        userFromDB = new User();
        userFromDB.setUsername(user.getUsername());
        userFromDB.setPassword(passwordEncoder.encode(user.getPassword()));
        userFromDB.setEmail(user.getEmail());
        userFromDB.setActivationCode(UUID.randomUUID().toString());
        Role userRole = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found"));
        userFromDB.getRoles().add(userRole);
        userRepository.save(userFromDB);
        if(!StringUtils.isEmpty(user.getEmail())){
            String message = String.format("Hello! \n" +
                            "Welcome to Narek's website. Please visit the following link to activate your account: http://localhost:8080/activate/%s",
                    userFromDB.getActivationCode());
            mailSender.send(user.getEmail(), "Activation code", message);
        }
        return true;
    }

    public boolean activateUser(String code){
        User user = userRepository.findUserByActivationCode(code);
        if(user == null){
            return false;
        }
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }
}
