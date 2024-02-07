package com.aayush.pds.controller;

import com.aayush.pds.model.UserEncrypted;
import com.aayush.pds.model.UserEntity;
import com.aayush.pds.service.LoginService;
import com.aayush.pds.service.RegistrationService;
import com.aayush.pds.utility.AES;
import com.aayush.pds.utility.DHKeys;
import com.aayush.pds.utility.RSAKeys;
import com.aayush.pds.utility.UserDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600)
@RestController
public class RegistrationController {

  private final RegistrationService registrationService;
  private final DHKeys dhKeys;
  private final RSAKeys rsaKeys;
  private UserEntity userEntity;
  private final UserDTO userDTO;
  private final AES aes;

  @Autowired
  public RegistrationController(
    RegistrationService registrationService,
    DHKeys dhKeys,
    RSAKeys rsaKeys,
    UserEntity userEntity,
    UserEncrypted userEncrypted,
    UserDTO userDTO,
    LoginService loginService,
    AES aes
  ) {
    this.registrationService = registrationService;
    this.dhKeys = dhKeys;
    this.rsaKeys = rsaKeys;
    this.userEntity = userEntity;
    this.userDTO = userDTO;
    this.aes = aes;
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public ResponseEntity<Map<String, Integer>> registerUser(
    @RequestBody UserEncrypted userEncrypted
  ) throws Exception {
    try {
      UserEntity userEntity = new UserEntity();
      BigInteger b = new BigInteger(userEncrypted.getUserPublicKey(), 16);
      String newClientPublicKey = b.toString(10); //Convert user hex	public key to decimal
      userEntity.setUserPublicKey(newClientPublicKey);
      String dh_SHASharedKey = dhKeys.generateSharedKey(userEntity); // Generate DH Shared Secret Key !
      System.out.println(
        "SHAServerSharedKey = " + dhKeys.getDhSHAServerSharedKey()
      );

      // retrieves decrypted user credentials as json string.
      String jsonStrDecryptedUserCredentials = aes.decryptData(
        dhKeys.getDhSHAServerSharedKey(),
        userEncrypted.getEncryptedData()
      );
      JsonObject jsonObject = new Gson()
        .fromJson(jsonStrDecryptedUserCredentials, JsonObject.class);

      //Set UserTable fields for userEntity object.
      userEntity.setPassword(jsonObject.get("password").getAsString());
      userEntity.setUserName(jsonObject.get("username").getAsString());
      userEntity.setRsaUserpublickey(userEncrypted.getRsaUserPublicKey());
      userEntity.setSHASharedKey(dh_SHASharedKey);

      String rsaDecryptedSignature = rsaKeys.getRSADecryptedData(
        userEncrypted.getRsaSignature()
      ); //decrypting the Digital Signature.
      String SHAOfUserCredentials = dhKeys.generateSHA256(
        jsonStrDecryptedUserCredentials
      ); // generating SHA256 of decrypted data i.e username and password.
      System.out.println("SHA Hash = " + SHAOfUserCredentials);

      if (rsaDecryptedSignature.equals(SHAOfUserCredentials)) { // comparing SHA of Digital Signature with SHA of username and password
        Map<String, Integer> response = new HashMap<>();
        registrationService.saveUser(userEntity); // if equal than store the userEntity to database (usertable).
        response.put("userId", userEntity.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
      } else {
        Map<String, Integer> response = new HashMap<>();
        response.put("Signature Does not match", 401);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
      }
    } catch (
      InvalidKeyException
      | NoSuchAlgorithmException
      | NoSuchPaddingException
      | InvalidKeySpecException
      | IllegalBlockSizeException
      | BadPaddingException
      | IllegalArgumentException e
    ) {
      Map<String, Integer> response = new HashMap<>();
      response.put(e.getMessage(), 401);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
  }

  @RequestMapping(value = "/initSecure", method = RequestMethod.GET)
  public UserDTO getServerKeys() throws IOException {
    //returns diffie-hellman server public key and RSA server public key
    userDTO.setDhServerPublicKey(dhKeys.getServerPublicKey().toString(16));
    userDTO.setRsaServerPublicKey(rsaKeys.getRsaServerPublicKey());
    return userDTO;
  }
}
