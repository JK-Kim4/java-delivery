package com.tutomato.delivery.common.utils.cipher;

import java.util.EnumMap;
import java.util.Map;

public final class CryptoCipherFactory {

    private static final Map<CryptoAlgorithm, CryptoCipher> CIPHER_MAP
        = new EnumMap<>(CryptoAlgorithm.class);

    static {
        register(new Sha256CryptoCipher());
    }

    private CryptoCipherFactory() {
        throw new AssertionError("Cannot instantiate CryptoCipherFactory");
    }

    private static void register(CryptoCipher cipher) {
        CIPHER_MAP.put(cipher.algorithm(), cipher);
    }

    public static CryptoCipher get(CryptoAlgorithm algorithm) {
        CryptoCipher cipher = CIPHER_MAP.get(algorithm);
        if (cipher == null) {
            throw new IllegalArgumentException("지원하지 않는 암호화 알고리즘입니다: " + algorithm);
        }
        return cipher;
    }
}