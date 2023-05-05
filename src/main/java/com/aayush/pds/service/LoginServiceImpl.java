package com.aayush.pds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aayush.pds.model.UserEntity;
import com.aayush.pds.repository.RegistrationRepository;



@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	RegistrationRepository registrationRepository;
	
	
	@Override
	public boolean existByUserName(String userName) {

		return registrationRepository.existsByUserName(userName);
	}


	@Override
	public UserEntity getUserEntity(int id) {
		
		return registrationRepository.getReferenceById(id);
	}


	@Override
	public String getSharedSecretKey(int id) {
		
		return registrationRepository.getSharedSecretKey(id);
	}
	
	
}
