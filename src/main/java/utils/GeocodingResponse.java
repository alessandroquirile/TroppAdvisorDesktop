package utils;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class GeocodingResponse {
    private double latitude;
    private double longitude;

    public GeocodingResponse() {
    }

    public GeocodingResponse(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "GeocodingResponse{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
