package dao_implementations;

import dao_interfaces.RestaurantDAO;
import models.Restaurant;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class RestaurantDAO_MongoDB implements RestaurantDAO {
    @Override
    public boolean add(Restaurant restaurant) {
        // codice per inserire un ristorante su mongodb
        return true;
    }

    @Override
    public boolean delete(Restaurant restaurant) {
        // codice per eliminare un ristorante su mongodb
        return true;
    }

    @Override
    public boolean update(Restaurant restaurant) {
        // codice per aggiornare un ristorante su mongodb
        return true;
    }
}
