package application.admin.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TeacherInformationForm {

    private static Stage teacherInfoForm;

    public void showForm() throws IOException {
        if (teacherInfoForm == null) {
            teacherInfoForm = new Stage();

            AnchorPane root = FXMLLoader.load(getClass().getResource("/application/admin/TeacherInformation.fxml"));

            // Close button
            Button closeButton = new Button("\u2718");
            closeButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: black;" +
                "-fx-font-size: 16px;" +
                "-fx-border-width: 0;" +
                "-fx-cursor: hand;"
            );

            closeButton.setOnMouseEntered(e -> closeButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: red;" +
                "-fx-font-size: 18px;" +
                "-fx-border-width: 0;" +
                "-fx-cursor: hand;"
            ));

            closeButton.setOnMouseExited(e -> closeButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: black;" +
                "-fx-font-size: 16px;" +
                "-fx-border-width: 0;" +
                "-fx-cursor: hand;"
            ));

            closeButton.setOnAction(e ->
            showAlert(AlertType.WARNING, "Please Fill Teacher's Information.!", null, null)
            		);
            StackPane closePane = new StackPane(closeButton);
            closePane.setAlignment(Pos.TOP_RIGHT);
            closePane.setPadding(new Insets(5));

            AnchorPane mainLayout = new AnchorPane();
            mainLayout.getChildren().addAll(root, closePane);

            AnchorPane.setTopAnchor(closePane, 0.0);
            AnchorPane.setRightAnchor(closePane, 0.0);

            Scene sc = new Scene(mainLayout, 670, 350);
            teacherInfoForm.setScene(sc);
            teacherInfoForm.initStyle(StageStyle.UNDECORATED);
            teacherInfoForm.setX(500);
            teacherInfoForm.setY(200);
        }

        teacherInfoForm.show();
    }
    public void closeForm() {
        if (teacherInfoForm != null) {
            teacherInfoForm.close();
        }
    }
    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}
