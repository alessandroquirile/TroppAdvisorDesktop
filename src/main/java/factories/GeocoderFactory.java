package factories;

import geocoder_implementations.Geocoder_APIPositionStack;
import geocoder_interfaces.Geocoder;
import my_exceptions.TechnologyNotSupportedYetException;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class GeocoderFactory {
    private static GeocoderFactory singletonInstance = null;

    private GeocoderFactory() {

    }

    public static synchronized GeocoderFactory getInstance() {
        if (singletonInstance == null)
            singletonInstance = new GeocoderFactory();
        return singletonInstance;
    }

    public Geocoder getGeocoder(String geocoderTechnology) {
        if (geocoderTechnology.equals("api_position_stack"))
            return new Geocoder_APIPositionStack();
        else
            throw new TechnologyNotSupportedYetException(geocoderTechnology);
    }
}
