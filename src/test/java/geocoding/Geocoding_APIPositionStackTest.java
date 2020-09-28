package geocoding;

import junit.framework.TestCase;

import java.io.IOException;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class Geocoding_APIPositionStackTest extends TestCase {

    public void testForward() throws IOException, InterruptedException {
        Geocoding_APIPositionStack geocoding_apiPositionStack = new Geocoding_APIPositionStack();
        geocoding_apiPositionStack.forward("Via Salvo D'Acquisto 25 San Giorgio a Cremano Napoli 80046 Italia");
    }
}