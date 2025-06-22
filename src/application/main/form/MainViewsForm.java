package application.main.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainViewsForm {
    public static Stage mainForm = null;

    public void toggleMainViewsForm() throws IOException {
        if (mainForm == null || !mainForm.isShowing()) {
            openMainViewsForm();
        } else {
            mainForm.close();
            mainForm = null;
        }
    }

    private void openMainViewsForm() throws IOException {
        mainForm = new Stage();
        BorderPane root = FXMLLoader.load(getClass().getResource("/application/main/MainViews.fxml"));

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
            mainForm.close();
            mainForm = null;
        });

        // Place close button
        StackPane closePane = new StackPane(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(5));

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(root, closePane);
        AnchorPane.setTopAnchor(closePane, 5.0);
        AnchorPane.setRightAnchor(closePane, 5.0);

        Scene sc = new Scene(mainLayout, 1366, 700);
        mainForm.setScene(sc);
        mainForm.setTitle("Option Form !");
        mainForm.initStyle(StageStyle.UNDECORATED);
        mainForm.show();
    }
}
