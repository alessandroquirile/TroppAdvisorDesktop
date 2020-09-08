package dao_interfaces;

import models.Restaurant;

import java.io.File;
import java.io.IOException;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface ImageDAO {
    boolean deleteAllImagesFromRestaurant(Restaurant restaurant) throws IOException, InterruptedException;

    boolean deleteThisImage(String imageUrl) throws IOException, InterruptedException;

    String loadFile(File file) throws IOException;

    boolean updateRestaurantImage(Restaurant restaurant, String endpoint) throws IOException, InterruptedException;
}
