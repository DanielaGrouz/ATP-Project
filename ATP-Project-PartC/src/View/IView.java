package View;

import ViewModel.MyViewModel;

import java.net.URL;
import java.util.ResourceBundle;

public interface IView {

    void initialize(URL url, ResourceBundle resourceBundle);
    void setViewModel(MyViewModel viewModel);

    }
