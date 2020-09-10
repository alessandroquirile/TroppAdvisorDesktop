package dao_interfaces;

import models.Accomodation;

import java.io.IOException;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface CityDAO {
    boolean insert(Accomodation accomodation) throws IOException, InterruptedException;

    boolean delete(Accomodation accomodation) throws IOException, InterruptedException;
}
