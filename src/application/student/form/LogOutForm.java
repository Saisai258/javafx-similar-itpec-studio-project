package application.student.form;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LogOutForm {
	public static Stage logOutStage = null; // Track the form state

	public void toggleLogOutForm() throws IOException {
		if (logOutStage == null || !logOutStage.isShowing()) {
			openLogOutForm();
		} else {
			logOutStage.close();
			logOutStage = null;
		}
	}

	private void openLogOutForm() throws IOException {
		logOutStage = new Stage();
		AnchorPane root = FXMLLoader.load(getClass().getResource("/application/student/LogOut.fxml"));
		Scene scene = new Scene(root, 360, 200);
		logOutStage.setScene(scene);
        logOutStage.getIcons().add(new Image("downLogo.jpg"));
		logOutStage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
            	logOutStage.close();
            }
        });
		logOutStage.initStyle(StageStyle.UNDECORATED);
		logOutStage.setX(990);
		logOutStage.setY(125);
		logOutStage.show();
	}
}
