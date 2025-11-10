package com.tutomato.delivery.common.utils.cipher;

public interface CryptoCipher {

    CryptoAlgorithm algorithm();

    String encrypt(String plainText);

    boolean matches(String plainText, String encryptedText);

}
