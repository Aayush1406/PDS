package com.aayush.pds.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aayush.pds.model.UserEntity;

import jakarta.annotation.PostConstruct;

@Component
public class DHKeys {

	private BigInteger dhPrimeNumber;
	private BigInteger dhG;
	private BigInteger dhServerPrivateKey;
	private BigInteger dhServerPublicKey;
	private BigInteger dhServerSharedKey;
	private String dhSHAServerSharedKey;
	private static final String SHA_256 = "SHA-256";

	@Autowired
	UserEntity userEntity;

	public BigInteger getClientPrimeNumber() {
		return dhPrimeNumber;
	}

	public void setClientPrimeNumber(BigInteger clientPrimeNumber) throws IOException {
		this.dhPrimeNumber = clientPrimeNumber;

	}

	public BigInteger getClientG() {
		return dhG;
	}

	public void setClientG(BigInteger clientG) throws IOException {
		this.dhG = clientG;

	}

	public BigInteger getServerPrivateKey() {
		return dhServerPrivateKey;
	}

	public void setServerPrivateKey(BigInteger serverPrivateKey) throws IOException {
		this.dhServerPrivateKey = serverPrivateKey;

	}

	public BigInteger getServerPublicKey() {
		return dhServerPublicKey;
	}

	public void setServerPublicKey(BigInteger serverPublicKey) throws IOException {

		this.dhServerPublicKey = serverPublicKey;

	}

	public BigInteger getServerSharedKey() {
		return dhServerSharedKey;
	}

	public void setServerSharedKey(BigInteger serverSharedKey) throws IOException {
		this.dhServerSharedKey = serverSharedKey;

	}

	public String getDhSHAServerSharedKey() {
		return dhSHAServerSharedKey;
	}

	public void setDhSHAServerSharedKey(String dhSHAServerSharedKey) {
		this.dhSHAServerSharedKey = dhSHAServerSharedKey;
	}

	@PostConstruct
	public void generateDHExchangeKeys() throws IOException {

		File gFile = new File("dh_g.pem");
		File primeNumberFile = new File("dh_primeNumber.pem");
		readPemFile(gFile);
		readPemFile(primeNumberFile);

		String[] fileNames = { "dh_priv.pem", "dh_pub.pem" };
		for (String fileNamePointer : fileNames) {
			File f = new File(fileNamePointer);
			if (f.exists() == false) {

				dhServerPrivateKey = generateRandomNumber();
				setServerPrivateKey(dhServerPrivateKey);
				createPemFile(this.dhServerPrivateKey.toString(), "dh_priv.pem");
				createPemFile(this.dhServerPrivateKey.toString(16), "dh_privhex.pem");

				dhServerPublicKey = getClientG().modPow((dhServerPrivateKey), getClientPrimeNumber());
				setServerPublicKey(dhServerPublicKey);
				createPemFile(this.dhServerPublicKey.toString(), "dh_pub.pem");
				createPemFile(this.dhServerPublicKey.toString(16), "dh_pubhex.pem");

			} else {
				readPemFile(f);
			}
		}
	}

	public String generateSharedKey(UserEntity userEntity) throws IOException, NoSuchAlgorithmException {

		dhServerSharedKey = new BigInteger(userEntity.getUserPublicKey()).modPow((getServerPrivateKey()),
				getClientPrimeNumber());
		setServerSharedKey(dhServerSharedKey);
		System.out.println("Shared Key Final in Hex format = " + getServerSharedKey().toString(16));
		String SHAServerSharedKey = generateSHA256(getServerSharedKey().toString(16));
		setDhSHAServerSharedKey(SHAServerSharedKey);
		return SHAServerSharedKey;
	}

	public byte[] hexStringToByteArray(String hexString) {
		int length = hexString.length();
		byte[] data = new byte[length / 2];
		for (int i = 0; i < length; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
					+ Character.digit(hexString.charAt(i + 1), 16));
		}
		return data;
	}

	public String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	public String generateSHA256(String secretKey) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance(SHA_256);
		byte[] hash = digest.digest(secretKey.getBytes(StandardCharsets.UTF_8));

		return bytesToHex(hash);
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
		if (file.getPath().equals("dh_priv.pem")) {
			setServerPrivateKey(new BigInteger(value));
		}
		if (file.getPath().equals("dh_g.pem")) {
			setClientG(new BigInteger(value));
		}
		if (file.getPath().equals("dh_primeNumber.pem")) {
			setClientPrimeNumber(new BigInteger(value));
		}
		if (file.getPath().equals("dh_pub.pem")) {
			setServerPublicKey(new BigInteger(value));
		}
	}

	public BigInteger generateRandomNumber() {
		SecureRandom rand = new SecureRandom();
		BigInteger num = new BigInteger(128, rand);
		return num;
	}

	public BigInteger generateRandomPrimeNumber() {
		SecureRandom rand = new SecureRandom();
		BigInteger num = BigInteger.probablePrime(512, rand);
		return num;
	}
}
