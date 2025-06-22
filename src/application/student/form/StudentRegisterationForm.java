package application.student.form;

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

public class StudentRegisterationForm {

    public static Stage rForm;

    public void toggleRegistrationForm() throws IOException {
        if (rForm == null || !rForm.isShowing()) {
            openRegistrationForm();
        } else {
            closeForm();
        }
    }

    private void openRegistrationForm() throws IOException {
        rForm = new Stage();

        AnchorPane root = FXMLLoader.load(getClass().getResource("/application/student/StudentRegistration.fxml"));

        // Close button (❌)
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

        closeButton.setOnAction(e -> closeForm());

        // Layout for button
        StackPane closePane = new StackPane(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(5));

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(root, closePane);
        AnchorPane.setTopAnchor(closePane, 5.0);
        AnchorPane.setRightAnchor(closePane, 5.0);

        Scene scene = new Scene(mainLayout, 700, 600);
        rForm.setX(433);
        rForm.setY(125);
        rForm.getIcons().add(new Image("downLogo.jpg"));
        rForm.setScene(scene);
        rForm.setTitle("Student Registration Form");
        rForm.initStyle(StageStyle.UNDECORATED);
        rForm.show();

        // Optional: auto-close when window loses focus
//        rForm.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
//            if (!isNowFocused) {
//                closeForm();
//            }
//        });
    }

    // Static method to close externally
    public static void closeForm() {
        if (rForm != null) {
            rForm.close();
            rForm = null;
        }
    }
}
