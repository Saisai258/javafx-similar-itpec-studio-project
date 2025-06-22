package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ScrollPaneForm {

	public static Stage pane;
	public Stage scrollPaneForm() throws IOException {
		pane=new Stage();
		AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/teacher/ScrollPane.fxml"));
		Scene sc=new Scene(root,1050,600);
		pane.setX(270);
		pane.setY(130);
        pane.getIcons().add(new Image("downLogo.jpg"));
		pane.setScene(sc);
		pane.setTitle("Exam schedule Form!");
		pane.initStyle(StageStyle.UNDECORATED);
		// Close the window when clicking outside
		pane.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
            	pane.close();
            }
        });
		return pane;
		
	} 
}
