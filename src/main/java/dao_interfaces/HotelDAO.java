package dao_interfaces;

import models.Hotel;

import java.io.IOException;
import java.util.List;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface HotelDAO {
    boolean add(Hotel hotel) throws IOException, InterruptedException;

    List<Hotel> retrieveAt(int page, int size) throws IOException, InterruptedException;

    List<Hotel> retrieveAt(String query, int page, int size) throws IOException, InterruptedException;

    boolean delete(Hotel hotel) throws IOException, InterruptedException;

    boolean update(Hotel hotel) throws IOException, InterruptedException;

    boolean deleteImage(Hotel hotel, String imageUrl) throws IOException, InterruptedException;

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean updateImage(Hotel hotel, String endpoint) throws IOException, InterruptedException;
}