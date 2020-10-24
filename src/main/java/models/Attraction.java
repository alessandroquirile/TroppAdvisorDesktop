package models;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class Attraction extends Accomodation {
    private String openingTime;

    public Attraction() {

    }

    public Attraction(Accomodation accomodation) {
        super(accomodation);
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    @Override
    public String toString() {
        return "Attraction{" +
                "openingTime='" + openingTime + '\'' +
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
                ", hasCertificateOfExcellence=" + certificateOfExcellence +
                ", addedDate='" + addedDate + '\'' +
                ", lastModificationDate='" + lastModificationDate + '\'' +
                '}';
    }
}
