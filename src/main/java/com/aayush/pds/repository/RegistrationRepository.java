package com.aayush.pds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aayush.pds.model.UserEntity;

@Repository
public interface RegistrationRepository extends JpaRepository<UserEntity, Integer>{
	
	boolean existsByUserName(String userName);

	@Query(value="Select shared_key from usertable where user_id = ?1", nativeQuery = true)
	String getSharedSecretKey(int id);
	
}
