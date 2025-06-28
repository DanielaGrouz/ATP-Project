package View;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

/**
 * Utility class for displaying information and error dialogs in the UI.
 */
public class UIUtils {

    /**
     * Shows an information dialog with the given message.
     *
     * @param msg The message to display in the information dialog.
     */
    public static void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait(); //display the dialog and wait for user to close it
    }

    /**
     * Shows an error dialog with expandable details.
     *
     * @param errorText The error details to display in the dialog.
     */
    public static void showError(String errorText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An unexpected error occurred \uD83D\uDE2D");
        alert.setContentText("Details are available below:");

        //create a non-editable, wrapped text area for error details
        TextArea textArea = new TextArea(errorText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        //allow the text area to expand to fill available space
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        //set the text area as the expandable content of the dialog
        alert.getDialogPane().setExpandableContent(textArea);
        alert.getDialogPane().setExpanded(true);

        alert.showAndWait(); //display the dialog and wait for user to close it
    }

}
