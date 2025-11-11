package com.tutomato.delivery.common.utils.cipher;

public enum CryptoAlgorithm {
    SHA256("SHA-256"), SHA512("SHA-512"),;

    private final String cryptoAlgorithm;

    CryptoAlgorithm(String cryptoAlgorithm) {
        this.cryptoAlgorithm = cryptoAlgorithm;
    }

    public String getCryptoAlgorithm() {
        return cryptoAlgorithm;
    }
}
