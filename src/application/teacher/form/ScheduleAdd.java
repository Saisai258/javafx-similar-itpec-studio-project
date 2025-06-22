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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ScheduleAdd {

    public static Stage schedule;

    private double xOffset = 0;
    private double yOffset = 0;

    public void toggleScheduleForm() throws IOException {
        if (schedule == null || !schedule.isShowing()) {
            openScheduleForm();
        } else {
            closeForm();
        }
    }

    private void openScheduleForm() throws IOException {
        schedule = new Stage();

        GridPane root = FXMLLoader.load(getClass().getResource("/application/teacher/ScheduleAdd.fxml"));

        // Close button ❌
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

        // Position the close button
        StackPane closePane = new StackPane(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(5));

        AnchorPane wrapper = new AnchorPane();
        wrapper.getChildren().addAll(root, closePane);
        AnchorPane.setTopAnchor(closePane, 0.0);
        AnchorPane.setRightAnchor(closePane, 0.0);

        // Draggable window
        wrapper.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        wrapper.setOnMouseDragged((MouseEvent event) -> {
            schedule.setX(event.getScreenX() - xOffset);
            schedule.setY(event.getScreenY() - yOffset);
        });

        Scene scene = new Scene(wrapper, 770, 400);
        schedule.setX(398);
        schedule.setY(225);
        schedule.getIcons().add(new Image("downLogo.jpg"));
        schedule.setScene(scene);
        schedule.setTitle("Exam Schedule Add Form!");
        schedule.initStyle(StageStyle.UNDECORATED);
        schedule.show();

//        // Optional: auto-close when unfocused
//        schedule.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
//            if (!isNowFocused) {
//                closeForm();
//            }
//        });
    }

    public static void closeForm() {
        if (schedule != null) {
            schedule.close();
            schedule = null;
        }
    }

}
