package services;

import java.security.MessageDigest;
import java.util.Random;

public class Token {

    public static String generateToken() {

        try {
            Random randomNumber = new Random();
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(randomNumber.toString().getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
