package com.aayush.pds.utility;

import java.util.List;

public class Post {

	private List<Integer> postIds;
	private String SNDigitalSignature;
	private int userId;

	public List<Integer> getPostIds() {
		return postIds;
	}

	public void setPostIds(List<Integer> postIds) {
		this.postIds = postIds;
	}

	public String getSNDigitalSignature() {
		return SNDigitalSignature;
	}

	public void setSNDigitalSignature(String sNDigitalSignature) {
		SNDigitalSignature = sNDigitalSignature;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
