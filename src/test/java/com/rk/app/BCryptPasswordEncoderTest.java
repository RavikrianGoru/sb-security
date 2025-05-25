package com.rk.app;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderTest {
	public static void main(String[] args) {
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		String encodedPWD = bpe.encode("admin@123");
		System.out.println(encodedPWD);
		encodedPWD = bpe.encode("ns@123");
		System.out.println(encodedPWD);
		encodedPWD = bpe.encode("vs@123");
		System.out.println(encodedPWD);
	}
}
