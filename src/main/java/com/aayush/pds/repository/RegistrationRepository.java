package com.aayush.pds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aayush.pds.model.UserEntity;

@Repository
public interface RegistrationRepository extends JpaRepository<UserEntity, Integer>{

	
}
