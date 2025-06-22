package application.student.form;

import java.io.IOException;
import java.util.function.Consumer;

import application.student.controller.ColorPickerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ColorPickerForm {
    public static void showColorPicker(Consumer<Color> colorCallback, Consumer<String> avatarCallback) throws IOException {
    	
    	 FXMLLoader loader = new FXMLLoader(ColorPickerController.class.getResource("/application/student/ColorPicker.fxml"));
        Parent root = loader.load();

        ColorPickerController controller = loader.getController();
        controller.setColorCallback(colorCallback);  // Set the color callback
        controller.setAvatarCallback(avatarCallback);  // Set the avatar callback

        Stage stage = new Stage();
        stage.setTitle("Choose Color and Avatar");
        stage.setScene(new Scene(root,480,600));
        stage.setX(543);
        stage.setY(125);
        stage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
            	stage.close();
            }
        });
        stage.getIcons().add(new Image("downLogo.jpg"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    

}
