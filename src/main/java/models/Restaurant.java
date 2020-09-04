package models;

import java.util.List;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class Restaurant extends Accomodation {
    private List<String> typeOfCuisine;

    public List<String> getTypeOfCuisine() {
        return typeOfCuisine;
    }

    public void setTypeOfCuisine(List<String> typeOfCuisine) {
        this.typeOfCuisine = typeOfCuisine;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "typeOfCuisine=" + typeOfCuisine +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avarageRating=" + avarageRating +
                ", avaragePrice=" + avaragePrice +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address=" + address +
                ", point=" + point +
                ", reviews=" + reviews +
                ", images=" + images +
                ", hasCertificateOfExcellence=" + hasCertificateOfExcellence +
                ", addedDate='" + addedDate + '\'' +
                ", lastModificationDate='" + lastModificationDate + '\'' +
                '}';
    }
}
