package com.marco.labs.ppmapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.marco.labs.ppmapp.domain.User;
import com.marco.labs.ppmapp.exceptions.UsernameAlreadyExistException;
import com.marco.labs.ppmapp.exceptions.UsernameAlreadyExistResponse;
import com.marco.labs.ppmapp.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		try {
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			//Username has to be unique(exception)
			newUser.setUsername(newUser.getUsername());
			//make sure that password and confirmPassword match
			//we don't persist or show the confirmPassword
			newUser.setConfirmPassword("");
			return userRepository.save(newUser);
			
		}catch(Exception e) {
			throw new UsernameAlreadyExistException("Username '"+ newUser.getUsername()+ "' already exists");
		}		
	}
}
