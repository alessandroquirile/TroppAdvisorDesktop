package models;

import java.util.List;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class Restaurant extends Accomodation {
    private List<String> typeOfCuisine;
    private String openingTime;

    public List<String> getTypeOfCuisine() {
        return typeOfCuisine;
    }

    public void setTypeOfCuisine(List<String> typeOfCuisine) {
        this.typeOfCuisine = typeOfCuisine;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getCity() {
        return this.address.getCity();
    }

    public String getStreet() {
        return this.address.getStreet();
    }

    public String getHouseNumber() {
        return this.address.getHouseNumber();
    }

    public String getPostalCode() {
        return this.address.getPostalCode();
    }

    public String getProvince() {
        return this.address.getProvince();
    }

    public String getTypeOfAddress() {
        return this.address.getType();
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "typeOfCuisine=" + typeOfCuisine +
                ", openingTime='" + openingTime + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avarageRating=" + avarageRating +
                ", avaragePrice=" + avaragePrice +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address=" + address +
                ", point=" + point +
                ", reviews=" + reviews +
                ", totalReviews=" + totalReviews +
                ", images=" + images +
                ", hasCertificateOfExcellence=" + hasCertificateOfExcellence +
                ", addedDate='" + addedDate + '\'' +
                ", lastModificationDate='" + lastModificationDate + '\'' +
                '}';
    }
}
