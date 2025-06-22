package application.main.form;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;


public class HomeForm {
	
	public AnchorPane homesForm() throws IOException {
		AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/main/Home.fxml"));
	
		return root;
		
	}
}
