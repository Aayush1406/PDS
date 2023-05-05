package com.aayush.pds.model;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;


@Component
public class PostEncrypted {

	private String encryptedContent;

	private String postDigitalSignature;

	private LocalDateTime createdAt;

	public String getEncryptedContent() {
		return encryptedContent;
	}

	public void setEncryptedContent(String content) {
		this.encryptedContent = content;
	}

	public String getPostDigitalSignature() {
		return postDigitalSignature;
	}

	public void setDigitalSignature(String postDigitalSignature) {
		this.postDigitalSignature = postDigitalSignature;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
