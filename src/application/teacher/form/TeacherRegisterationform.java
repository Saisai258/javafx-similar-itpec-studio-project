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

public class TeacherRegisterationform {

    private static Stage trStage = null;

    public void toggleTeacherRegistrationForm() throws IOException {
        if (trStage == null || !trStage.isShowing()) {
            openTeacherRegistrationForm();
        } else {
            trStage.close();
            trStage = null;
        }
    }

    private void openTeacherRegistrationForm() throws IOException {
        trStage = new Stage();

        AnchorPane root = FXMLLoader.load(getClass().getResource("/application/teacher/TeacherRegisterationForm.fxml"));

        // Create close button
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
            trStage.close();
            trStage = null;
        });

        // Position close button
        StackPane closePane = new StackPane(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(5));

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(root, closePane);
        AnchorPane.setTopAnchor(closePane, 5.0);
        AnchorPane.setRightAnchor(closePane, 5.0);

        Scene scene = new Scene(mainLayout, 700, 600);
        trStage.setX(433);
        trStage.setY(125);
        trStage.getIcons().add(new Image("downLogo.jpg"));
        trStage.setScene(scene);
        trStage.setTitle("Teacher Registration Form");
        trStage.initStyle(StageStyle.UNDECORATED); // Remove default window borders
        trStage.show();

    }
    public static void closeForm() {
        if (trStage != null && trStage.isShowing()) {
        	trStage.close();
        	trStage = null;
        }
    }

}
