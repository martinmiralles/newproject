package com.martin.ppmtool.services;

import com.martin.ppmtool.domain.User;
import com.martin.ppmtool.exceptions.UsernameAlreadyExistsException;
import com.martin.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){

        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

            //Username has to be unique (exception)
            newUser.setUsername(newUser.getUsername());

            //Make sure that password and confirmPassword match

            //Don't persist / show the confirmPassword
            newUser.setConfirmPassword("");

            return userRepository.save(newUser);
        } catch (Exception e){
            throw new UsernameAlreadyExistsException("Username '" + newUser.getUsername() + "' already exists");
        }


    }

}
