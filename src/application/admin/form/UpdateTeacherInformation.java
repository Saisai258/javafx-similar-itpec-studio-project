package application.admin.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UpdateTeacherInformation {

	public static Stage infoForm;

	public Stage updateTeacherInformation() throws IOException {
		infoForm = new Stage();
		AnchorPane root = (AnchorPane) FXMLLoader
				.load(getClass().getResource("/application/admin/UpdateOfTeacherInformation.fxml"));
		Button closeButton = new Button("\u2718"); // X symbol for close

		closeButton.setStyle("-fx-background-color: transparent; " + "-fx-text-fill: black; " + "-fx-font-size: 16px; "
				+ "-fx-border-width: 0; " + "-fx-cursor: hand;");

		closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-background-color: transparent; "
				+ "-fx-text-fill: red; " + "-fx-font-size: 18px; " + "-fx-border-width: 0; " + "-fx-cursor: hand;"));

		closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-background-color: transparent; "
				+ "-fx-text-fill: black; " + "-fx-font-size: 16px; " + "-fx-border-width: 0; " + "-fx-cursor: hand;"));

		closeButton.setOnAction(e -> infoForm.close()); // Close the window on button click

		StackPane closePane = new StackPane(closeButton);
		closePane.setAlignment(Pos.TOP_RIGHT);
		closePane.setPadding(new Insets(5));

		AnchorPane mainLayout = new AnchorPane();
		mainLayout.getChildren().addAll(root, closePane);
		AnchorPane.setTopAnchor(closePane, 5.0);
		AnchorPane.setRightAnchor(closePane, 5.0);

		Scene sc = new Scene(mainLayout, 750, 500);
		infoForm.setX(408);
		infoForm.setY(150);
		infoForm.setScene(sc);
		infoForm.setTitle("Update Teacher Information Form !");
		infoForm.initModality(javafx.stage.Modality.WINDOW_MODAL);
		infoForm.initStyle(StageStyle.UNDECORATED);
		return infoForm;

	}
	public static void closeForm() {
        if (infoForm != null && infoForm.isShowing()) {
        	infoForm.close();
        	infoForm = null;
        }
    }
}
