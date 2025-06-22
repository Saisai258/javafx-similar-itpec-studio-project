package application.admin.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DashForm {
	private static Stage uForm;	
	public static BorderPane root;
	
	public DashForm() {
		
	}
	
	public Stage dashForm() throws IOException {
		uForm = new Stage();
		root = (BorderPane)FXMLLoader.load(getClass().getResource("/application/AdminDash.fxml"));
		Scene sc = new Scene(root,1000,650);
        uForm.getIcons().add(new Image("downLogo.jpg"));
        uForm.setTitle("Quiz Mania Ultimate Challenge");
		uForm.setScene(sc);
		return uForm;
		
	}
	
	public static void closeDash() {
        if (uForm != null) {
            uForm.close();
            uForm = null; // Clear reference
        }
    }
}
