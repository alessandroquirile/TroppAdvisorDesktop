package dao_interfaces;

import models.Accomodation;

import java.io.File;
import java.io.IOException;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface ImageDAO {
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean deleteAllImages(Accomodation accomodation) throws IOException, InterruptedException;

    boolean delete(String imageUrl) throws IOException, InterruptedException;

    String load(File file) throws IOException;
}