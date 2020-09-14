package models;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class Hotel extends Accomodation {
    private Integer stars;

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "stars=" + stars +
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
