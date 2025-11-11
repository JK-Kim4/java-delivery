package com.tutomato.delivery.common.utils.cipher;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256CryptoCipher implements CryptoCipher {

    @Override
    public CryptoAlgorithm algorithm() {
        return CryptoAlgorithm.SHA256;
    }

    @Override
    public String encrypt(String plainText) {
        return digest(plainText);
    }

    @Override
    public boolean matches(String plainText, String encryptedText) {
        if (plainText == null || encryptedText == null) {
            return false;
        }
        return encrypt(plainText).equals(encryptedText);
    }

    private String digest(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance(CryptoAlgorithm.SHA256.getCryptoAlgorithm());
            byte[] hash = md.digest(plainText.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(CryptoAlgorithm.SHA256.getCryptoAlgorithm() + " 알고리즘을 사용할 수 없습니다.", e);
        }
    }
}
