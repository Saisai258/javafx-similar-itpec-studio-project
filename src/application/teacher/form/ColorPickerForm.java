package application.teacher.form;

import java.io.IOException;
import java.util.function.Consumer;

import application.teacher.controller.ColorPickerController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ColorPickerForm {
	public static Stage stage;
    public static void showColorPicker(Consumer<Color> colorCallback, Consumer<String> avatarCallback) throws IOException {
    	
    	 FXMLLoader loader = new FXMLLoader(ColorPickerController.class.getResource("/application/teacher/ColorPicker.fxml"));
        Parent root = loader.load();

        ColorPickerController controller = loader.getController();
        controller.setColorCallback(colorCallback);  // Set the color callback
        controller.setAvatarCallback(avatarCallback);  // Set the avatar callback

        stage = new Stage();
        stage.setTitle("Choose Color and Avatar");
        stage.setScene(new Scene(root));
        stage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
            	stage.close();
            }
        });
        Button closeButton = new Button("\u2718");  // X symbol for close
        
        closeButton.setStyle(
            "-fx-background-color: transparent; " + 
            "-fx-text-fill: black; " + 
            "-fx-font-size: 16px; " + 
            "-fx-border-width: 0; " + 
            "-fx-cursor: hand;"
        );

        // Simplified hover effect
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
        
        closeButton.setOnAction(e -> stage.close()); // Close the window on button click

        // Layout for close button
        StackPane closePane = new StackPane(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(5));

        // Wrap the UI with the close button on top
        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(root, closePane);
        AnchorPane.setTopAnchor(closePane, 5.0);
        AnchorPane.setRightAnchor(closePane, 5.0);

        // Set scene and initialize the stage
        Scene sc = new Scene(mainLayout, 478, 605);
        stage.setX(543);
        stage.setY(125);
        stage.getIcons().add(new Image("downLogo.jpg"));
        stage.setScene(sc);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    

}
