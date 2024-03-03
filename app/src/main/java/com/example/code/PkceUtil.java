package com.example.code;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import android.util.Base64;

public class PkceUtil {

    // Generate a secure random code verifier
    public static String generateCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        return Base64.encodeToString(codeVerifier, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
    }

    // Generate the code challenge from the code verifier
    public static String generateCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes, 0, bytes.length);
        byte[] digest = md.digest();
        return Base64.encodeToString(digest, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);
        System.out.println(codeVerifier);
        System.out.println(codeChallenge);
    }
}
