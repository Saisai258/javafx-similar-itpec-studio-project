package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BeforeUpdateQuestionFormCopy {

    public static Stage buqform;

    private double xOffset = 0;
    private double yOffset = 0;

    public BeforeUpdateQuestionFormCopy() {
    }

    public void toggleForm() throws IOException {
        if (buqform == null || !buqform.isShowing()) {
            openForm();
        } else {
            closeForm();
        }
    }

    private void openForm() throws IOException {
        buqform = new Stage();

        AnchorPane root = FXMLLoader.load(getClass().getResource("/application/teacher/BeforeUpdateQuestionCopy.fxml"));

        // Create ❌ close button
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

        // Add close button to top-right
        StackPane closePane = new StackPane(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(5));

        AnchorPane wrapper = new AnchorPane();
        wrapper.getChildren().addAll(root, closePane);
        AnchorPane.setTopAnchor(closePane, 0.0);
        AnchorPane.setRightAnchor(closePane, 0.0);

        // Make draggable
        wrapper.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        wrapper.setOnMouseDragged((MouseEvent event) -> {
            buqform.setX(event.getScreenX() - xOffset);
            buqform.setY(event.getScreenY() - yOffset);
        });

        Scene scene = new Scene(wrapper, 800, 550);
        buqform.setX(383);
        buqform.setY(150);
        buqform.setScene(scene);
        buqform.getIcons().add(new Image("downLogo.jpg"));
        buqform.initStyle(StageStyle.UNDECORATED);
        buqform.show();

        // Auto-close when unfocused (optional)
        buqform.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                closeForm();
            }
        });
    }

    public static void closeForm() {
        if (buqform != null) {
            buqform.close();
            buqform = null;
        }
    }
}
