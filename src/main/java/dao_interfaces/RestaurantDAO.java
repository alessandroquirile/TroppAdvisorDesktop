package dao_interfaces;

import models.Restaurant;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface RestaurantDAO {
    boolean add(Restaurant restaurant);

    boolean delete(Restaurant restaurant);

    boolean update(Restaurant restaurant);
}
