package com.aayush.pds.service;

import java.util.List;

import com.aayush.pds.model.PostEntity;

public interface PostService {

	void savePostEntity(PostEntity postEntity);

	List<PostEntity> getPostsByTopic(int topicId);

	void likePost(int postId);

	PostEntity getPost(int postId);

	String getViewerSharedKey(int userId);

	String getPostSharedKeyById(int userId);

	String getViewerRSAPublicKey(int userId);

	
}
