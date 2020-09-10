package controllers;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public abstract class CrudController extends Controller {
    public abstract void setListenerOn(Button button);

    public abstract void setListenerOnTableView(TableView<Object> tableView);

    public abstract void setViewsAsDefault();

    public abstract void setListenerOnListView(ListView<String> listViewFotoPath);
}
