package ir.aminer.potadoshack.core.order;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable {
    private String name;
    private String address;

    public Address(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address other = (Address) o;
        return Objects.equals(name, other.name) &&
                Objects.equals(address, other.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    @Override
    public String toString() {
        return name;
    }
}
