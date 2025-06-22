package application.admin.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ChangePasswordForm {

	public static  Stage changePasswordForm;

	public ChangePasswordForm() {
		// TODO Auto-generated constructor stub
	}
	
	public  Stage changePasswordForm() throws IOException {			
		changePasswordForm = new Stage();
		AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/admin/ChangePassword.fxml"));

        // Set scene and initialize the stage
        Scene sc = new Scene(root, 340, 320); 
		changePasswordForm.setScene(sc);
        changePasswordForm.getIcons().add(new Image("downLogo.jpg"));
		changePasswordForm.initStyle(StageStyle.UNDECORATED);
		changePasswordForm.setX(613);
		changePasswordForm.setY(240);
		changePasswordForm.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
            	changePasswordForm.close();
            }
        });
		changePasswordForm.show();
		return changePasswordForm;
		
	}
}
