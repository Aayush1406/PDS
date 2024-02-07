package com.aayush.pds;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PdsApplication {

  public static void main(String[] args)
    throws IOException, NoSuchAlgorithmException {
    SpringApplication.run(PdsApplication.class, args);
  }
}
