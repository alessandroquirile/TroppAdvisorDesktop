package factories;

import dao_implementations.AccountDAO_Cognito;
import dao_implementations.CityDAO_MongoDB;
import dao_implementations.ImageDAO_S3;
import dao_implementations.RestaurantDAO_MongoDB;
import dao_interfaces.AccountDAO;
import dao_interfaces.CityDAO;
import dao_interfaces.ImageDAO;
import dao_interfaces.RestaurantDAO;
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
}