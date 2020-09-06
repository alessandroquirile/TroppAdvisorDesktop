package dao_interfaces;

import models.Restaurant;

import java.io.IOException;
import java.util.List;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface RestaurantDAO {
    boolean add(Restaurant restaurant) throws IOException, InterruptedException;

    List<Restaurant> retrieve(int page, int size) throws IOException, InterruptedException;

    boolean delete(Restaurant restaurant) throws IOException, InterruptedException;

    boolean update(Restaurant restaurant);
}
