package com.aayush.pds.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.aayush.pds.model.UserEntity;

@Component
public class ServerKey {

	private BigInteger client_Prime_Number;
	private BigInteger client_G;
	private BigInteger server_Private_Key;
	private BigInteger server_Public_Key ;
	private BigInteger server_Shared_Key;


	public BigInteger getClientPrimeNumber() {
		return client_Prime_Number;
	}

	public void setClientPrimeNumber(BigInteger clientPrimeNumber) throws IOException {
		this.client_Prime_Number = clientPrimeNumber;
		System.out.println("clientPrimeNumber.pem file created !");
	}

	public BigInteger getClientG() {
		return client_G;
	}

	public void setClientG(BigInteger clientG) throws IOException {
		this.client_G = clientG;
		System.out.println("clientG.pem file created !");
	}

	public BigInteger getServerPrivateKey() {
		return server_Private_Key;
	}

	public void setServerPrivateKey(BigInteger serverPrivateKey) throws IOException {
		this.server_Private_Key = serverPrivateKey;
		System.out.println("serverPrivateKey.pem file created !");
	}

	public BigInteger getServerPublicKey() {
		return server_Public_Key;
	}

	public void setServerPublicKey(BigInteger serverPublicKey) throws IOException {
		
		this.server_Public_Key = serverPublicKey;
		System.out.println("serverPublicKey.pem file created !");
	}

	public BigInteger getServerSharedKey() {
		return server_Shared_Key;
	}

	public void setServerSharedKey(BigInteger serverSharedKey) throws IOException {
		this.server_Shared_Key = serverSharedKey;
		System.out.println("serverSheredKey.pem file created !");
	}
	
	public void generateSharedKey(UserEntity userEntity) throws IOException {
		String[] fileNames = {"serverPrivateKey.pem","serverPublicKey.pem"}; //check if this file are present at the location
		for(String fileName:fileNames) {
			File file = new File(fileName);
			if(file.exists()==false) {
				File primenumfile = new File("clientPrimeNumber.pem");				
				readPemFile(primenumfile); // read from clientPrimeNumber.pem
				
				File gfile = new File("clientG.pem");				
				readPemFile(gfile); // read from clientG.pem
				
				BigInteger serverPrivateNum = generateRandomNumber(); //generate a random private key number
				setServerPrivateKey(serverPrivateNum);	
				createPemFile(this.server_Private_Key.toString(),"serverPrivateKey.pem"); //create serverPrivateKey.pem 
				
				server_Public_Key = getClientG().modPow((server_Private_Key),getClientPrimeNumber()); //server_Public_Key has datatype of BigInteger here
				System.out.println("serverPublicKey = "+server_Public_Key);
				setServerPublicKey(server_Public_Key);
				createPemFile(this.server_Public_Key.toString(),"serverPublicKey.pem"); //create serverPublicKey.pem 
				
			}
			readPemFile(file);
		}
		File primenumfile = new File("clientPrimeNumber.pem");
		
		readPemFile(primenumfile);
		
		File gfile = new File("clientG.pem");
		
		readPemFile(gfile);
		
			//Here client public key is in decimal form as we are performing mathematical function.
		  server_Shared_Key = new BigInteger(userEntity.getUserPublicKey()).modPow((getServerPrivateKey()),getClientPrimeNumber());
		  setServerSharedKey(server_Shared_Key);
		 
		  // Here I am converting shared key to hex form to match with meets secret key.
		  System.out.println("server shared key = "+server_Shared_Key.toString(16));
		  System.out.println("g = "+getClientG());
		  
		  //Here I am sending this hex form serverPublicKey to Meet. 
		  System.out.println("server public key = "+getServerPublicKey().toString(16));
	}
	
	public void createPemFile(String pemData, String fileName) throws IOException {
	
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName)));
        writer.write("-----BEGIN-----\n");
        writer.write(pemData);
        writer.write("\n-----END-----");
        writer.close();
    }
	
	public void readPemFile(File file) throws IOException {
	
		boolean flag = false;
		System.out.println(file);
		String value="";
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(file.getPath())))) {
            String line;
   //         System.out.println(file);
            while ((line = reader.readLine()) != null) {
            	if(line.equals("-----BEGIN-----") == false && line.equals("-----END-----")==false) {
            		System.out.println("whilw"+line);
            		value = line;
            		flag = true;
            	}
            	if(flag==true)
            		break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		System.out.println("Value = "+value);
		if(file.getPath().equals("serverPrivateKey.pem")) {
			setServerPrivateKey(new BigInteger(value));
		}if(file.getPath().equals("clientG.pem")) {
			setClientG(new BigInteger(value));
		}if(file.getPath().equals("clientPrimeNumber.pem")) {
			setClientPrimeNumber(new BigInteger(value));
		}if(file.getPath().equals("serverPublicKey.pem")) {
			setServerPublicKey(new BigInteger(value));
		}
	}
		
	
	
	public BigInteger generateRandomNumber() {
		
		
		  SecureRandom rand = new SecureRandom(); BigInteger num = new BigInteger(128,rand); 
		  return num;
		 
		
	}
	
	public BigInteger generateRandomPrimeNumber() {
		SecureRandom rand = new SecureRandom();
		BigInteger num = BigInteger.probablePrime(512, rand);
		return num;
	}
}
