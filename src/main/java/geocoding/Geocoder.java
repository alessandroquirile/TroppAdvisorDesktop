package geocoding;

import utils.GeocodingResponse;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface Geocoder {
    GeocodingResponse forward(String address);
}
