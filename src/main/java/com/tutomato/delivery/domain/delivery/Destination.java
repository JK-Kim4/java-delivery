package com.tutomato.delivery.domain.delivery;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.util.Objects;

@Embeddable
public class Destination {

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "zipCode", column = @Column(name = "destination_zip_code", length = 10)),
        @AttributeOverride(name = "address1", column = @Column(name = "destination_address1", length = 255)),
        @AttributeOverride(name = "address2", column = @Column(name = "destination_address2", length = 255))
    })
    private Address address;

    protected Destination() {
    }

    private Destination(Address address) {
        this.address = address;
    }

    public static Destination create(Address address) {
        return new Destination(address);
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Destination that = (Destination) o;
        return Objects.equals(getAddress(), that.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getAddress());
    }
}
