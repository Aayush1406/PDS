package com.aayush.pds.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aayush.pds.model.PostEncrypted;
import com.aayush.pds.model.PostEntity;
import com.aayush.pds.model.Topic;
import com.aayush.pds.model.UserEntity;
import com.aayush.pds.service.LoginService;
import com.aayush.pds.service.PostService;
import com.aayush.pds.utility.AES;
import com.aayush.pds.utility.DHKeys;
import com.aayush.pds.utility.Post;
import com.aayush.pds.utility.PostDTO;
import com.aayush.pds.utility.RSAKeys;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PostController {

	private final PostService postService;
	private final PostEntity postEntity;
	private final DHKeys dhKeys;
	private UserEntity userEntity;
	private final LoginService loginService;
	private final AES aes;
	private final RSAKeys rsaKeys;

	@Autowired
	public PostController(PostService postService, PostEntity postEntity, PostEncrypted postEncrypted, DHKeys dhKeys,
			 Topic topic, UserEntity userEntity, LoginService loginService, AES aes, RSAKeys rsaKeys) {
		this.postService = postService;
		this.postEntity = postEntity;
		this.dhKeys = dhKeys;
		this.userEntity = userEntity;
		this.loginService = loginService;
		this.aes = aes;
		this.rsaKeys = rsaKeys;
	}

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Integer>> createPost(@RequestHeader int userId,@RequestBody PostEncrypted postEncrypted) throws Exception {

		setSharedKeyOfUser(userId); // Setting the shared key of client everytime
		String decryptedPostContent = aes.decryptData(dhKeys.getDhSHAServerSharedKey(),postEncrypted.getEncryptedContent()); //decrypt post content
		
		
		//setting the fields in postEntity object for posttable database.
		postEntity.setContent(postEncrypted.getEncryptedContent());
		LocalDateTime now = LocalDateTime.now();
		postEntity.setCreatedAt(now);
		userEntity = loginService.getUserEntity(userId);
		postEntity.setUserEntity(userEntity);
		postEntity.setDigitalSignature(postEncrypted.getPostDigitalSignature());
		

		String rsaPostContentDecryptedSignature = rsaKeys.getRSADecryptedData(postEncrypted.getPostDigitalSignature()); // decrypt the digital signature to get SHA256
		String postContentSHA = dhKeys.generateSHA256(decryptedPostContent);

		if (rsaPostContentDecryptedSignature.equals(postContentSHA)) { // comparing SHA of signature to SHA of post content.

			postService.savePostEntity(postEntity); //saving the post to posttable.
			Map<String, Integer> response = new HashMap<>();
			response.put("PostId", postEntity.getPostId());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		} else {
			Map<String, Integer> response = new HashMap<>();
			response.put("Error", 401);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

		}
	}

	
	@RequestMapping(value = "/posts/list", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> getPostList(@RequestBody Post post) throws Exception {

		List<Integer> postIdsList = post.getPostIds(); // get the list of post ids from SN
		
		String viewerSharedKey = postService.getViewerSharedKey(post.getUserId()); // fetch viewer shared key from usertable
		String viewerRSAPublicKey = postService.getViewerRSAPublicKey(post.getUserId()); //fetch viewer rsa public from usertable

		List<PostDTO> decryptedPostList = new ArrayList<>();
		for (int i = 0; i < postIdsList.size(); i++) {

			PostDTO postDTO = new PostDTO();
			
			int postId = postIdsList.get(i);
			PostEntity postEntity = postService.getPost(postId); //retrieveing posts with ids = postIdsList

			int postContentId = postEntity.getPostId(); 
			
			postDTO.setPostId(postContentId);
			
			String encryptedContent = postEntity.getContent(); 

			int userId = postEntity.getUserEntity().getUserId(); //retrieving the userId of a post with a particular postId i.e in the list

			String contentSharedKey = postService.getPostSharedKeyById(userId); //fetching the shared key of a user who created the post

			String decryptedPostContent = aes.decryptData(contentSharedKey, encryptedContent); //decrypting the post with shared key of user who created the actual post.
			postDTO.setPostContent(decryptedPostContent);

			decryptedPostList.add(postDTO); //adding the postDTO object which consists of postId and postContent.

		}
		
		String jsonStrArrayOfPost = convertListToJson(decryptedPostList); // converting list to json String
		String encryptedPostContentList = aes.encryptData(viewerSharedKey, jsonStrArrayOfPost); //encrypting the json string using viewer shared secret key

		String postContentListSHA256 = dhKeys.generateSHA256(jsonStrArrayOfPost); // generating SHA256 of json string i.e array of posts 
		String postContentDigitalSignature = rsaKeys.encrypt(postContentListSHA256, viewerRSAPublicKey); // encrypting SHA256 with viewer RSA public key.

		System.out.println("Encrypted Post List = " + encryptedPostContentList);
		System.out.println("Post Content Digital Signature = " + postContentDigitalSignature);

		Map<String, String> postListresponse = new HashMap<String, String>();
		postListresponse.put("data", encryptedPostContentList);
		postListresponse.put("signature", postContentDigitalSignature);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(postListresponse);

	}

	public String convertListToJson(List<PostDTO> list) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(list);
	}


	public String getDuration(LocalDateTime createdAt) {
		Duration duration = Duration.between(createdAt, LocalDateTime.now());
		long seconds = duration.getSeconds();
		String relativeTime = "";
		if (seconds < 60) {
			relativeTime = seconds + " seconds ago";
		} else if (seconds < 3600) {
			long minutes = seconds / 60;
			relativeTime = minutes + " minutes ago";
		} else if (seconds < 86400) {
			long hours = seconds / 3600;
			relativeTime = hours + " hours ago";
		} else {
			long days = seconds / 86400;
			relativeTime = days + " days ago";
		}

		return relativeTime;
	}

	public void setSharedKeyOfUser(int userId) {

		String Secretvalue = loginService.getSharedSecretKey(userId);
		dhKeys.setDhSHAServerSharedKey(Secretvalue);

	}
}
