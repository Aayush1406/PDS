package com.aayush.pds.utility;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class Test{

    private static final String ALGORITHM = "Twofish";
    private static final String MODE = "ECB";
    private static final String PADDING = "PKCS5Padding";
    private static final String TRANSFORMATION = ALGORITHM + "/" + MODE + "/" + PADDING;

    // Replace this key with your own 512-byte key
    private static final byte[] SECRET_KEY = hexStringToByteArray("e4bd85a21b7ba423c4a96d091dab437bc7837baada5aac64b45100e605c87eb77d48499c9325ee069a72393bf51b3a6a0f3a4b3ee2bbd4744a842fdd800e10596d9c98453a2111c20e656fcb0513b5cacd16d05adfca627063caa688e5ba65f55c6cde77864de639dbfa84ca443cb321026653ea0377aef3321ce8cc149b88221fa1b51bf8eaaaa6491ec0922d80ad3b30e867e151809e9cbc90bde09cf0e135b646cf27d6c1a2561540dd3bcfbab01c97590b11c7eb51caab09e2bfc1554507eaac74288962916a94668cb625fca57697c0133598d9c8bbcaaa46f88af57eefe9a6eb79a720ef83b25d78cbabbad4acbfbe565b2d5c8bcb99bb4cf3d99e0e05");

    public static String encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY, ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data);
        return Base64.encodeBase64String(encrypted);
    }

    public static byte[] decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY, ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(Base64.decodeBase64(encryptedData));
        return decrypted;
    }

    // Helper method to convert hex string to byte array
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


    
    public static void main(String[] args) throws Exception {
      //  SpringApplication.run(MyApplication.class, args);
        String plaintext = "Hello World!";
        String encryptedText = Test.encrypt(plaintext.getBytes());
        System.out.println("Encrypted Text: " + encryptedText);
        byte[] decryptedText = Test.decrypt(encryptedText);
        System.out.println("Decrypted Text: " + new String(decryptedText));
    }
 
}
