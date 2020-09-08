package dao_interfaces;

import models.Restaurant;

import java.io.IOException;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface CityDAO {
    boolean insert(Restaurant restaurant) throws IOException, InterruptedException;

    boolean delete(Restaurant restaurant) throws IOException, InterruptedException;
}
