package geocoding;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.byteowls.jopencage.model.JOpenCageReverseRequest;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class Geocoder {
    public static void forwardGeocodingDemo() {
        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("dba40429c8ae43b78ae293bc0d221fb5");

        JOpenCageReverseRequest request = new JOpenCageReverseRequest(41.40015, 2.15765);
        request.setLanguage("it"); // prioritize results in a specific language using an IETF format language code (italian)
        request.setNoDedupe(true); // don't return duplicate results
        request.setLimit(5); // only return the first 5 results (default is 10)
        request.setNoAnnotations(true); // exclude additional info such as calling code, timezone, and currency
        request.setMinConfidence(3); // restrict to results with a confidence rating of at least 3 (out of 10)

        JOpenCageResponse response = jOpenCageGeocoder.reverse(request);

        // get the formatted address of the first result:
        String formattedAddress = response.getResults().get(0).getFormatted();

        System.out.println(formattedAddress);
    }

    public static void reverseGeocodingDemo() {
        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("dba40429c8ae43b78ae293bc0d221fb5");
        JOpenCageForwardRequest request = new JOpenCageForwardRequest("Via Salvo D'Acquisto, 25, San Giorgio a Cremano, 80046, Napoli, Italia");

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);
        JOpenCageLatLng firstResultLatLng = response.getFirstPosition(); // get the coordinate pair of the first result

        System.out.println(firstResultLatLng.getLat() + ", " + firstResultLatLng.getLng());
    }

    public static JOpenCageLatLng reverseGeocoding(String address) {
        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("dba40429c8ae43b78ae293bc0d221fb5");
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(address);

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);

        //System.out.println(firstResultLatLng.getLat() + ", " + firstResultLatLng.getLng());

        return response.getFirstPosition();
    }
}
