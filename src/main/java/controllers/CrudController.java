package controllers;

import dao_interfaces.ImageDAO;
import factories.DAOFactory;
import factories.FormCheckerFactory;
import factories.GeocoderFactory;
import form_checker_interfaces.FormChecker;
import geocoding.Geocoder;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public abstract class CrudController extends Controller {
    protected DAOFactory daoFactory;
    protected int currentPage = 0;
    protected int currentPageSize = 150;
    protected FormCheckerFactory formCheckerFactory;
    protected FormChecker formChecker;
    protected GeocoderFactory geocoderFactory;
    protected Geocoder geocoder;
    protected boolean retrieving = false;
    protected boolean modifying = false;
    protected ObservableList<String> imagesSelectedToDelete;
    protected ImageDAO imageDAO;

    public abstract void setListenerOn(Button button);

    public abstract void setListenerOnTableView(TableView<Object> tableView);

    public abstract void setViewsAsDefault();

    public abstract void setListenerOnListView(ListView<String> listViewFotoPath);
}