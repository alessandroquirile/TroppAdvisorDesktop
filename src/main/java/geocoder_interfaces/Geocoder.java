package geocoder_interfaces;

import utils.GeocodingResponse;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface Geocoder {
    GeocodingResponse forward(String address);
}