package View;

import Model.IModel;
import Model.MyModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ViewModel.MyViewModel;
import javafx.scene.image.Image;

/**
 * Main class for launching the Maze JavaFX application.
 */
public class main extends Application {

    /**
     * This method is called when the JavaFX application starts.
     * It loads the FXML, sets up the Model and ViewModel, connects them to the View,
     * and displays the main window.
     *
     * @param primaryStage The main window stage.
     * @throws Exception if FXML or resources cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        //load the FXML layout for the main view
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();

        //get the controller for the view
        MyViewController myViewController = fxmlLoader.getController();

        //create the Model and ViewModel
        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        //connect the ViewModel to the View
        MyViewController view = fxmlLoader.getController();
        view.setViewModel(viewModel);
        viewModel.addObserver(view);

        //set the application icon
        primaryStage.getIcons().add(new Image("file:./ATP-Project-PartC/resources/images/icon.png"));

        //set window title and size
        primaryStage.setTitle("Maze Project");
        primaryStage.setScene(new Scene(root, 1000, 650));

        //handle window close event
        primaryStage.setOnCloseRequest(myViewController::exit);

        //show the application window
        primaryStage.show();
    }

    /**
     * The main entry point for the application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
