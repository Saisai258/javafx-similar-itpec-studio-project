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

public class StudentSignInForm {

    private static Stage sForm = null;

    public void toggleStudentSignInForm() throws IOException {
        if (sForm == null || !sForm.isShowing()) {
            openStudentSignInForm();
        } else {
            closeForm();
        }
    }

    private void openStudentSignInForm() throws IOException {
        sForm = new Stage();

        AnchorPane root = FXMLLoader.load(getClass().getResource("/application/student/SignIn.fxml"));

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

        closeButton.setOnAction(e -> closeForm());

        // StackPane for button alignment
        StackPane closePane = new StackPane(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(5));

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(root, closePane);
        AnchorPane.setTopAnchor(closePane, 5.0);
        AnchorPane.setRightAnchor(closePane, 5.0);

        Scene scene = new Scene(mainLayout, 700, 500);
        sForm.setX(433);
        sForm.setY(150);
        sForm.getIcons().add(new Image("downLogo.jpg"));
        sForm.setScene(scene);
        sForm.setTitle("Student Sign In Form");
        sForm.initStyle(StageStyle.UNDECORATED);
        sForm.show();

        // Optional: close when focus is lost
        sForm.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                closeForm();
            }
        });
    }

    // Static method to close from outside
    public static void closeForm() {
        if (sForm != null) {
            sForm.close();
            sForm = null;
        }
    }

    // Optional: check if form is open
    public static boolean isFormOpen() {
        return sForm != null && sForm.isShowing();
    }
}
