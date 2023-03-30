package com.aayush.pds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aayush.pds.model.UserEntity;
import com.aayush.pds.repository.RegistrationRepository;

@Service
public class RegistrationServiceImpl implements RegistrationService{

	@Autowired
	RegistrationRepository registrationRepository;
	
	@Override
	public UserEntity saveUser(UserEntity userEntity) {
	
		return registrationRepository.save(userEntity);
	}

	
	
}
