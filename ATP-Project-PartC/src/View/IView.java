package View;

import ViewModel.MyViewModel;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * An interface for all view classes (like controllers) in the application.
 * This allows for better separation between the view and the ViewModel,
 * and helps make the view code more modular and testable.
 */
public interface IView {

    /**
     * Called to initialize the view with FXML data.
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    void initialize(URL url, ResourceBundle resourceBundle);

    /**
     * Injects the ViewModel instance into the view, so it can bind and listen to data.
     * @param viewModel The ViewModel associated with this view.
     */
    void setViewModel(MyViewModel viewModel);

}
