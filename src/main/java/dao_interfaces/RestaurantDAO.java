package dao_interfaces;

import models.Restaurant;

import java.io.IOException;
import java.util.List;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface RestaurantDAO {
    boolean add(Restaurant restaurant) throws IOException, InterruptedException;

    List<Restaurant> retrieveAt(int page, int size) throws IOException, InterruptedException;

    boolean delete(Restaurant restaurant) throws IOException, InterruptedException;

    boolean update(Restaurant restaurant) throws IOException, InterruptedException;

    boolean deleteRestaurantSingleImageFromRestaurantCollection(Restaurant restaurant, String imageUrl) throws IOException, InterruptedException;

    boolean updateRestaurantSingleImageFromRestaurantCollection(Restaurant restaurant, String endpoint) throws IOException, InterruptedException;
}