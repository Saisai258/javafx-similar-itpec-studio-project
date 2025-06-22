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

public class RegisterationForm {
    public static Stage rForm = null;

    public void toggleRegisterationForm() throws IOException {
        if (rForm == null || !rForm.isShowing()) {
            openRegisterationForm(); // Open if not already open
        } else {
            rForm.close(); // Close if already open
            rForm = null;
        }
    }

    private void openRegisterationForm() throws IOException {
        rForm = new Stage();
        AnchorPane root = FXMLLoader.load(getClass().getResource("/application/admin/Registeration.fxml"));

        // Create close button (✘)
        Button closeButton = new Button("\u2718");

        closeButton.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: black; " +
            "-fx-font-size: 16px; " +
            "-fx-border-width: 0; " +
            "-fx-cursor: hand;"
        );

        closeButton.setOnMouseEntered(e -> closeButton.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: red; " +
            "-fx-font-size: 18px; " +
            "-fx-border-width: 0; " +
            "-fx-cursor: hand;"
        ));

        closeButton.setOnMouseExited(e -> closeButton.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: black; " +
            "-fx-font-size: 16px; " +
            "-fx-border-width: 0; " +
            "-fx-cursor: hand;"
        ));

        closeButton.setOnAction(e -> {
            rForm.close();
            rForm = null;
        });

        // Place close button
        StackPane closePane = new StackPane(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(5));

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(root, closePane);
        AnchorPane.setTopAnchor(closePane, 5.0);
        AnchorPane.setRightAnchor(closePane, 5.0);

        Scene scene = new Scene(mainLayout, 700, 600);
        rForm.getIcons().add(new Image("downLogo.jpg"));
        rForm.setX(433);
        rForm.setY(125);
        rForm.setScene(scene);
        rForm.initStyle(StageStyle.UNDECORATED); // Optional: can use UTILITY or TRANSPARENT
        rForm.show();

//        // Optional: Close form when it loses focus
//        rForm.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
//            if (!isNowFocused) {
//                rForm.close();
//                rForm = null;
//            }
//        });

    }
    public static void closeForm() {
        if (rForm != null && rForm.isShowing()) {
            rForm.close();
            rForm = null;
        }
    }

}
