package com.aayush.pds.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aayush.pds.model.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer>{

	@Query(value="select * from posttable where topic_id=?1 ", nativeQuery = true)
	List<PostEntity> getPostsByTopic(int topicId);

	@Modifying
	@Query(value="Update posttable set like_count = like_count+1 where id=?1", nativeQuery = true)
	void likePost(int postId);

	@Query(value="select * from posttable where id = ?1" , nativeQuery = true)
	PostEntity getPost(int postId);

	@Query(value="Select shared_key from usertable where user_id = ?1 ", nativeQuery = true)
	String getViewerSharedKey(int userId);

	@Query(value = "Select shared_key from usertable where user_id = ?1 ", nativeQuery = true)
	String getPostSharedKeyById(int userId);

	@Query(value = "Select rsa_userpublickey from usertable where user_id = ?1 ", nativeQuery = true)
	String getViewerRSAPublicKey(int userId);

}
