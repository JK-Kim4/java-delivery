package com.tutomato.delivery.domain.delivery;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public record Address(
    String zipCode,
    String address1,
    String address2
) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(zipCode(), address.zipCode()) && Objects.equals(
            address1(), address.address1()) && Objects.equals(address2(), address.address2());
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipCode(), address1(), address2());
    }

    @Override
    public String toString() {
        return "Address{" +
            "zipCode='" + zipCode + '\'' +
            ", address1='" + address1 + '\'' +
            ", address2='" + address2 + '\'' +
            '}';
    }
}
