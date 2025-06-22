package application.admin.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RegisterChoiceForm {
    public static Stage rForm = null; // Track the form state
    
    public void toggleRegisterChoiceForm() throws IOException {
        if (rForm == null || !rForm.isShowing()) {
            openRegisterChoiceForm(); // Open if it's not already open
        } else {
            rForm.close(); // Close if it's already open
            rForm = null; // Reset the reference
        }
    }

    private void openRegisterChoiceForm() throws IOException {
        rForm = new Stage();
        AnchorPane root = FXMLLoader.load(getClass().getResource("/application/admin/AdminRegisterChoice.fxml"));
        
        // Close button setup
        Button closeButton = new Button("\u2718");  // X symbol for close
        
        closeButton.setStyle(
            "-fx-background-color: transparent; " + 
            "-fx-text-fill: black; " + 
            "-fx-font-size: 16px; " + 
            "-fx-border-width: 0; " + 
            "-fx-cursor: hand;"
        );

        // Simplified hover effect
        closeButton.setOnMouseEntered(e -> closeButton.setStyle(
            "-fx-background-color: transparent; " + 
            "-fx-text-fill: red; " + 
            "-fx-font-size: 18px; " + 
            "-fx-border-width: 0; " + 
            "-fx-cursor: hand;"
        ));
        
        closeButton.setOnMouseExited(e -> closeButton.setStyle(
            "-fx-background-color: transparent; " + 
            "-fx-text-fill: black; " + 
            "-fx-font-size: 16px; " + 
            "-fx-border-width: 0; " + 
            "-fx-cursor: hand;"
        ));
        
        closeButton.setOnAction(e -> rForm.close()); // Close the window on button click

        // Layout for close button
        StackPane closePane = new StackPane(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(5));

        // Wrap the UI with the close button on top
        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(root, closePane);
        AnchorPane.setTopAnchor(closePane, 5.0);
        AnchorPane.setRightAnchor(closePane, 5.0);

        // Set scene and initialize the stage
        Scene sc = new Scene(mainLayout, 660, 450);  // Use the mainLayout (with close button)
        rForm.setX(453);
        rForm.setY(175);
        rForm.getIcons().add(new Image("downLogo.jpg"));
        rForm.setScene(sc);
        rForm.initStyle(StageStyle.UNDECORATED);  // Remove default window decoration
        rForm.show();

        // Close the window when it loses focus (optional)
        rForm.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                rForm.close();  // Close when it loses focus (you can remove this if not required)
            }
        });
    }
    public static void closeForm() {
        if (rForm != null && rForm.isShowing()) {
        	rForm.close();
        	rForm = null;
        }
    }
}
