package com.omar.backend.mymentor.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestPassword {

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        String password = "Nomeacuerdo.195";
		System.out.println("Password: " + password);
        System.out.println("Password en Base 64: " + passwordEncoder.encode(password));
	}
    
}
