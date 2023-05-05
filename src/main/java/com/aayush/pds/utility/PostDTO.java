package com.aayush.pds.utility;

import org.springframework.stereotype.Component;

@Component
public class PostDTO {

	private int postId;
	private String postContent;

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getPostContent() {
		return postContent;
	}

	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}

}
