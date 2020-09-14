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
                ", totalRating=" + totalRating +
                ", images=" + images +
                ", hasCertificateOfExcellence=" + hasCertificateOfExcellence +
                ", addedDate='" + addedDate + '\'' +
                ", lastModificationDate='" + lastModificationDate + '\'' +
                '}';
    }
}
