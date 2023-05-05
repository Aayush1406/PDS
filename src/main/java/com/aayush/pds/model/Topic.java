package com.aayush.pds.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Component
@Entity
@Table(name="topictable")
public class Topic {

	@Column(name="topic_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private int topicId;
	
	@Column(name="topic_title")
	private String title;
	
	/*
	 * @ManyToOne(fetch = FetchType.EAGER)
	 * 
	 * @JoinColumn(name="user_id",referencedColumnName="user_id") private UserEntity
	 * userEntity;
	 * 
	 * @OneToMany(mappedBy = "topic") private Set<TopicUser> topicUsers = new
	 * HashSet<>();
	 * 
	 * public UserEntity getUserEntity() { return userEntity; }
	 * 
	 * public void setUserEntity(UserEntity userEntity) { this.userEntity =
	 * userEntity; }
	 */
	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String titleName) {
		this.title = titleName;
	}
	
}
