package dao_interfaces;

import models.Accomodation;

import java.io.File;
import java.io.IOException;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface ImageDAO {
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean deleteAllAccomodationImagesFromBucket(Accomodation accomodation) throws IOException, InterruptedException;

    boolean deleteThisImageFromBucket(String imageUrl) throws IOException, InterruptedException;

    String loadFileIntoBucket(File file) throws IOException;
}