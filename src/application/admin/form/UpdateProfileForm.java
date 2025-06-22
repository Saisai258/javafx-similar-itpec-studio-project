package application.admin.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UpdateProfileForm {
	public static Stage updForm;
	
	public void toggleLogOutForm() throws IOException {
		if (updForm == null || !updForm.isShowing()) {
			updateProfile();
		} else {
			updForm.close();
			updForm = null;
		}
	}
	public Stage updateProfile() throws IOException {
		updForm = new Stage();
		AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/admin/UpdateProfile.fxml"));
		Scene sc = new Scene(root,800 ,500 );
		updForm.setScene(sc);
        updForm.getIcons().add(new Image("downLogo.jpg"));
		updForm.initStyle(StageStyle.UNDECORATED);
		updForm.setX(225);
        updForm.setY(127);
		return updForm;
		
	}
	public static void closeForm() {
        if (updForm != null && updForm.isShowing()) {
        	updForm.close();
        	updForm = null;
        }
    }
	public static boolean canBeSafelyClosed() {
        return true;
    }
	
	
}
