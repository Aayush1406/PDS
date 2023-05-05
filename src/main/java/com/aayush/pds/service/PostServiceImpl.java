package com.aayush.pds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aayush.pds.model.PostEntity;
import com.aayush.pds.repository.PostRepository;

import jakarta.transaction.Transactional;

@Service
public class PostServiceImpl implements PostService{

	@Autowired
	PostRepository postRepository;
	
	@Override
	public void savePostEntity(PostEntity postEntity) {
		
		postRepository.save(postEntity);
		
	}

	@Override
	public List<PostEntity> getPostsByTopic(int topicId) {
		
		return postRepository.getPostsByTopic(topicId);
	}

	@Transactional
	@Override
	public void likePost(int postId) {
	
		postRepository.likePost(postId);
	}

	@Override
	public PostEntity getPost(int postId) {

		return postRepository.getPost(postId);
	}

	@Override
	public String getViewerSharedKey(int userId) {
		
		return postRepository.getViewerSharedKey(userId);
	}


	@Override
	public String getPostSharedKeyById(int userId) {
		
		return postRepository.getPostSharedKeyById(userId);
	}

	@Override
	public String getViewerRSAPublicKey(int userId) {
		
		return postRepository.getViewerRSAPublicKey(userId);
	}

}
