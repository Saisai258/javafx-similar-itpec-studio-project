package application.admin.form;

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

public class SignInForm {

    private static Stage sForm = null;

    public void toggleSignInForm() throws IOException {
        if (sForm == null || !sForm.isShowing()) {
            openSignInForm();
        } else {
            sForm.close();
            sForm = null;
        }
    }

    private void openSignInForm() throws IOException {
        sForm = new Stage();

        AnchorPane root = FXMLLoader.load(getClass().getResource("/application/admin/SignIn.fxml"));

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
            sForm.close();
            sForm = null;
        });

        // StackPane to place close button
        StackPane closePane = new StackPane(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(5));

        // Combine layout
        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(root, closePane);
        AnchorPane.setTopAnchor(closePane, 0.0);
        AnchorPane.setRightAnchor(closePane, 0.0);

        Scene scene = new Scene(mainLayout, 700, 500);
        sForm.setX(433);
        sForm.setY(150);
        sForm.getIcons().add(new Image("downLogo.jpg"));
        sForm.setScene(scene);
        sForm.setTitle("Admin Sign In");
        sForm.initStyle(StageStyle.UNDECORATED);
        sForm.show();

        // Auto-close when focus is lost
        sForm.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                sForm.close();
                sForm = null;
            }
        });
    }
}
