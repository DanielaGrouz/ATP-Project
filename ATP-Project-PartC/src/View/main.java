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

public class main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        MyViewController myViewController = fxmlLoader.getController();
        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        view.setViewModel(viewModel);
        viewModel.addObserver(view);

        primaryStage.getIcons().add(new Image("file:./ATP-Project-PartC/resources/images/icon.png"));

        primaryStage.setTitle("Maze Project");
        primaryStage.setScene(new Scene(root, 1000, 650));
        primaryStage.setOnCloseRequest(myViewController::exit);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
