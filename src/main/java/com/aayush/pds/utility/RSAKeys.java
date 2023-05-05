package com.aayush.pds.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class RSAKeys {

	private String rsaServerPrivateKey;
	private String rsaServerPublicKey;

	public String getRsaServerPrivateKey() {
		return rsaServerPrivateKey;
	}

	public void setRsaServerPrivateKey(String rsaServerPrivateKey) {
		this.rsaServerPrivateKey = rsaServerPrivateKey;
	}

	public String getRsaServerPublicKey() {
		return rsaServerPublicKey;
	}

	public void setRsaServerPublicKey(String rsaServerPublicKey) {
		this.rsaServerPublicKey = rsaServerPublicKey;
	}

	@PostConstruct
	public void generateRSAExchangeKeys() throws NoSuchAlgorithmException, IOException {

		String[] fileNames = { "rsa_pub.pem", "rsa_priv.pem" };

		for (String fileNamePointer : fileNames) {

			File f = new File(fileNamePointer);
			if (f.exists() == false) {
				KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
				keyPairGenerator.initialize(1024);
				KeyPair keyPair = keyPairGenerator.generateKeyPair();
				PublicKey publicKey = keyPair.getPublic();
				PrivateKey privateKey = keyPair.getPrivate();

				byte[] publicKeyBytes = publicKey.getEncoded();
				// Convert the private key to base64
				String publicKeyBase64 = java.util.Base64.getEncoder().encodeToString(publicKeyBytes);

				byte[] privateKeyBytes = privateKey.getEncoded();
				// Convert the private key to base64
				String privateKeyBase64 = java.util.Base64.getEncoder().encodeToString(privateKeyBytes);

				createPemFile(privateKeyBase64, "rsa_priv.pem");
				createPemFile(publicKeyBase64, "rsa_pub.pem");

			}
			readPemFile(f);
		}

	}

	public String getRSADecryptedData(String encryptedMessageBase64)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,
			IllegalBlockSizeException, BadPaddingException {

		String privatekey = getRsaServerPrivateKey();
		byte[] encryptedMessage = java.util.Base64.getDecoder().decode(encryptedMessageBase64);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] privateKeyBytes = java.util.Base64.getDecoder().decode(privatekey);
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes = cipher.doFinal(encryptedMessage);
		String decryptedString = new String(decryptedBytes);

		System.out.println("Message Decrypted = " + decryptedString);
		return decryptedString;

	}

	public String encrypt(String plainText, String viewerRSAPublicKey) throws Exception {
		PublicKey publicKey = getPublicKey(viewerRSAPublicKey);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
		return java.util.Base64.getEncoder().encodeToString(encryptedBytes);
	}

	private PublicKey getPublicKey(String publicKeyString) throws Exception {
		byte[] publicKeyBytes = java.util.Base64.getDecoder().decode(publicKeyString);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(spec);
	}

	public void createPemFile(String pemData, String fileName) throws IOException {
		System.out.println("creating " + fileName + "!!");
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName)));
		writer.write("-----BEGIN-----\n");
		writer.write(pemData);
		writer.write("\n-----END-----");
		writer.close();
	}

	public void readPemFile(File file) throws IOException {
		System.out.println("reading " + file + "!!");
		boolean flag = false;
		String value = "";
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(file.getPath())))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.equals("-----BEGIN-----") == false && line.equals("-----END-----") == false) {

					value = line;
					flag = true;
				}
				if (flag == true)
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (file.getPath().equals("rsa_priv.pem")) {
			setRsaServerPrivateKey(value);
		}
		if (file.getPath().equals("rsa_pub.pem")) {
			setRsaServerPublicKey(value);
		}
	}

}
