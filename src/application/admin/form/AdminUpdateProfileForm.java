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

public class AdminUpdateProfileForm {
	public static  Stage auForm; // Not static to allow multiple instances

	public void toggleLogOutForm() throws IOException {
		if (auForm == null || !auForm.isShowing()) {
			adminUpdateProfileForm();
		} else {
			auForm.close();
			auForm = null;
		}
	}

	public Stage adminUpdateProfileForm() throws IOException {
			auForm = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/admin/UpdateProfile.fxml"));
			AnchorPane root = loader.load();	
			
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
		        
		        closeButton.setOnAction(e -> auForm.close()); // Close the window on button click

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
		        Scene sc = new Scene(mainLayout, 686 ,500);
	            auForm.getIcons().add(new Image("downLogo.jpg"));
			auForm.setScene(sc);
			auForm.setX(197);
			auForm.setY(127);
			auForm.initStyle(StageStyle.UNDECORATED);
			auForm.show();
		return auForm;
	}
	public static void closeForm() {
        if (auForm != null && auForm.isShowing()) {
        	auForm.close();
        	auForm = null;
        }
    }
	public static boolean canBeSafelyClosed() {
        return true;
    }
}
