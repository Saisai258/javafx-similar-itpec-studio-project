package application.admin.form;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProfileForm {
	public static Stage profileStage = null; // Track the form state
    private static boolean readyToClose = true;


	public void toggleProfileForm() throws IOException {
		
		if (profileStage == null || !profileStage.isShowing()) {
			openProfileForm();
		} else {
			profileStage.close();
			profileStage = null;
		}
		
	}

	private void openProfileForm() throws IOException {
		profileStage = new Stage();
		AnchorPane root = FXMLLoader.load(getClass().getResource("/application/admin/Profile.fxml"));
		Scene scene = new Scene(root, 290, 470);
        profileStage.getIcons().add(new Image("downLogo.jpg"));
		profileStage.setScene(scene);
		profileStage.initStyle(StageStyle.UNDECORATED);
		profileStage.setX(1052);
		profileStage.setY(127);
		profileStage.show();
		
	}
	public static void closeForm() {
        if (profileStage != null && profileStage.isShowing()) {
        	profileStage.close();
        	profileStage = null;
        }
    }
	// Check if profile form can be safely closed (used by TeacherHomeForm)
    public static boolean canBeSafelyClosed() {
        return readyToClose;
    }
	
}

