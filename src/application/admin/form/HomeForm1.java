package application.admin.form;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HomeForm1 {

	public static Stage homeStage;
	public static BorderPane root;

	public Stage adminForm() throws IOException {
		homeStage = new Stage();
		root = (BorderPane) FXMLLoader.load(getClass().getResource("/application/admin/Home.fxml"));
		homeStage.getIcons().add(new Image("downLogo.jpg"));
		homeStage.setTitle("Quiz Mania Ultimate Challenge");
		homeStage.setMaximized(true);
		Scene sc = new Scene(root, 1366, 700);
		homeStage.setScene(sc);
		homeStage.iconifiedProperty().addListener((obs, wasMinimized, isNowMinimized) -> {
			if (isNowMinimized && ProfileForm.canBeSafelyClosed()&&UpdateProfileForm.canBeSafelyClosed()) {
				Platform.runLater(() -> {
					ProfileForm.closeForm();
					UpdateProfileForm.closeForm();
				});
			}
		});

		homeStage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
			if (isNowFocused &&UpdateProfileForm.canBeSafelyClosed()) {
				Platform.runLater(() -> {
//					ProfileForm.closeForm();
					UpdateProfileForm.closeForm();
				});
			}
		});
		homeStage.setOnCloseRequest(event -> {
			ProfileForm.closeForm(); // Closes profile form if open
			UpdateProfileForm.closeForm();
		});
		return homeStage;

	}
}
