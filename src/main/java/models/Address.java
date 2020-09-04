package models;

import java.io.Serializable;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class Address implements Serializable {
    private String type;
    private String street;
    private String houseNumber;
    private String city;
    private String province;
    private String postalCode;

    public Address(String type, String street, String houseNumber, String city, String province, String postalCode) {
        this.type = type;
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "type='" + type + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
