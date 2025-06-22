package application.teacher.form;

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

public class TeacherLoginForm {

    private static Stage trlogform = null;

    public void toggleTeacherLoginForm() throws IOException {
        if (trlogform == null || !trlogform.isShowing()) {
            openTeacherLoginForm();
        } else {
            trlogform.close();
            trlogform = null;
        }
    }

    private void openTeacherLoginForm() throws IOException {
        trlogform = new Stage();

        AnchorPane root = FXMLLoader.load(getClass().getResource("/application/teacher/TeacherLoginForm.fxml"));

        // Close button
        Button closeButton = new Button("\u2718");
        closeButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: black;" +
            "-fx-font-size: 16px;" +
            "-fx-cursor: hand;"
        );

        closeButton.setOnMouseEntered(e -> closeButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: red;" +
            "-fx-font-size: 18px;" +
            "-fx-cursor: hand;"
        ));

        closeButton.setOnMouseExited(e -> closeButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: black;" +
            "-fx-font-size: 16px;" +
            "-fx-cursor: hand;"
        ));

        closeButton.setOnAction(e -> {
            trlogform.close();
            trlogform = null;
        });

        // StackPane for close button positioning
        StackPane closePane = new StackPane(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(5));

        // Combine layout
        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(root, closePane);
        AnchorPane.setTopAnchor(closePane, 5.0);
        AnchorPane.setRightAnchor(closePane, 5.0);

        Scene scene = new Scene(mainLayout, 700, 500);
        trlogform.setX(433);
        trlogform.setY(150);
        trlogform.getIcons().add(new Image("downLogo.jpg"));
        trlogform.setScene(scene);
        trlogform.setTitle("Teacher Login Form");
        trlogform.initStyle(StageStyle.UNDECORATED);
        trlogform.show();

        // Optional: Auto-close on focus loss
        trlogform.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                trlogform.close();
                trlogform = null;
            }
        });
    }
}
