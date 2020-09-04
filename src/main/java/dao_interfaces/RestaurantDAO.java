package dao_interfaces;

import models.Restaurant;

import java.io.IOException;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface RestaurantDAO {
    boolean add(Restaurant restaurant) throws IOException, InterruptedException;

    boolean delete(Restaurant restaurant);

    boolean update(Restaurant restaurant);
}
