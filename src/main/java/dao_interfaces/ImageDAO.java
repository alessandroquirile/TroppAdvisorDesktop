package dao_interfaces;

import models.Restaurant;

import java.io.File;
import java.io.IOException;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface ImageDAO {
    boolean deleteAllImagesFromBucket(Restaurant restaurant) throws IOException, InterruptedException;

    boolean deleteThisImageFromBucket(String imageUrl) throws IOException, InterruptedException;

    String loadFileIntoBucket(File file) throws IOException;
}