package com.tutomato.delivery.supporter;

import com.tutomato.delivery.common.utils.cipher.CryptoAlgorithm;
import com.tutomato.delivery.common.utils.cipher.CryptoCipher;
import com.tutomato.delivery.common.utils.cipher.CryptoCipherFactory;
import com.tutomato.delivery.domain.member.Member;
import com.tutomato.delivery.domain.member.Role;

public class MemberTestFixture {

    private final String RAW_PASSWORD_FIXTURE = "Ab12345678910!";
    private final CryptoCipher cipher = CryptoCipherFactory.get(CryptoAlgorithm.SHA256);

    private String account;
    private String name;

    public MemberTestFixture(String account, String name) {
        this.account = account;
        this.name = name;
    }


    public Member toStoreMember() {
        return Member.create(account, RAW_PASSWORD_FIXTURE, name, Role.STORE, cipher);
    }


    public Member toRiderMember() {
        return Member.create(account, RAW_PASSWORD_FIXTURE, name, Role.RIDER, cipher);
    }

}
