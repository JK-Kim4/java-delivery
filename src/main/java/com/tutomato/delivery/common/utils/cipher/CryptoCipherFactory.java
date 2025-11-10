package com.tutomato.delivery.common.utils.cipher;

import java.util.EnumMap;
import java.util.Map;

public class CryptoCipherFactory {

    private final Map<CryptoAlgorithm, CryptoCipher> cipherMap
        = new EnumMap<>(CryptoAlgorithm.class);

    public CryptoCipherFactory() {
        register(new Sha256CryptoCipher());
    }

    private void register(CryptoCipher cipher) {
        cipherMap.put(cipher.algorithm(), cipher);
    }

    public CryptoCipher get(CryptoAlgorithm algorithm) {
        CryptoCipher cipher = cipherMap.get(algorithm);
        if (cipher == null) {
            throw new IllegalArgumentException("지원하지 않는 암호화 알고리즘입니다: " + algorithm);
        }
        return cipher;
    }

}
