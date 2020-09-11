package dao_interfaces;

import models.Attraction;

import java.io.IOException;
import java.util.List;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface AttractionDAO {
    boolean add(Attraction attraction) throws IOException, InterruptedException;

    List<Attraction> retrieveAt(int page, int size) throws IOException, InterruptedException;

    boolean delete(Attraction attraction) throws IOException, InterruptedException;

    boolean update(Attraction attraction) throws IOException, InterruptedException;

    boolean deleteAttractionSingleImageFromAttractionCollection(Attraction attraction, String imageUrl) throws IOException, InterruptedException;

    boolean updateAttractionSingleImageFromAttractionCollection(Attraction attraction, String endpoint) throws IOException, InterruptedException;
}
