package application.admin.form;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class ForgotPasswordForm {

    public static Stage forgotForm;

    public Stage forgotPasswordForm() throws IOException {
        forgotForm = new Stage();
        
        // Load FXML content
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/admin/ForgetPassword.fxml"));
        AnchorPane root = loader.load();

        // Create a manual close button
        Button closeButton = new Button("\u2718");

        closeButton.setStyle(
        	    "-fx-background-color: transparent; " + 
        	    "-fx-text-fill: black; " + 
        	    "-fx-font-size: 16px; " + 
        	    "-fx-border-width: 0; " + 
        	    "-fx-cursor: hand;"
        	);

        	// Disable hover effect
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
        closeButton.setOnAction(e -> forgotForm.close());

        // Layout for close button
        StackPane closePane = new StackPane(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(5));

        // Wrap the UI with the close button on top
        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(root, closePane);
        AnchorPane.setTopAnchor(closePane, 5.0);
        AnchorPane.setRightAnchor(closePane, 5.0);

        // Set scene
        Scene sc = new Scene(mainLayout, 400, 250);
        forgotForm.getIcons().add(new Image("downLogo.jpg"));
        forgotForm.setScene(sc);
        forgotForm.initStyle(StageStyle.UNDECORATED); // Remove default window decorations
     // Auto-close when focus is lost
        forgotForm.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
            	forgotForm.close();
            	forgotForm = null;
            }
        });

        return forgotForm;
    }
}
