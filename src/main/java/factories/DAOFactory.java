package factories;

import dao_implementations.*;
import dao_interfaces.*;
import my_exceptions.TechnologyNotSupportedYetException;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class DAOFactory {

    private static DAOFactory daoFactorySingletonInstance = null;

    private DAOFactory() {

    }

    public static synchronized DAOFactory getInstance() {
        if (daoFactorySingletonInstance == null)
            daoFactorySingletonInstance = new DAOFactory();
        return daoFactorySingletonInstance;
    }

    public AccountDAO getAccountDAO(String accountStorageTechnology) {
        if (accountStorageTechnology.equals("cognito"))
            return new AccountDAO_Cognito();
        else
            throw new TechnologyNotSupportedYetException(accountStorageTechnology);
    }

    public RestaurantDAO getRestaurantDAO(String restaurantStorageTechnology) {
        if (restaurantStorageTechnology.equals("mongodb"))
            return new RestaurantDAO_MongoDB();
        else
            throw new TechnologyNotSupportedYetException(restaurantStorageTechnology);
    }

    public ImageDAO getImageDAO(String imageStorageTechnology) {
        if (imageStorageTechnology.equals("s3"))
            return new ImageDAO_S3();
        else
            throw new TechnologyNotSupportedYetException(imageStorageTechnology);
    }

    public CityDAO getCityDAO(String cityStorageTechnology) {
        if (cityStorageTechnology.equals("mongodb"))
            return new CityDAO_MongoDB();
        else
            throw new TechnologyNotSupportedYetException(cityStorageTechnology);
    }

    public HotelDAO getHotelDAO(String hotelStorageTechnology) {
        if (hotelStorageTechnology.equals("mongodb"))
            return new HotelDAO_MongoDB();
        else
            throw new TechnologyNotSupportedYetException(hotelStorageTechnology);
    }

    public AttractionDAO getAttractionDAO(String attractionStorageTechnology) {
        if (attractionStorageTechnology.equals("mongodb"))
            return new AttractionDAO_MongoDB();
        else
            throw new TechnologyNotSupportedYetException(attractionStorageTechnology);
    }
}