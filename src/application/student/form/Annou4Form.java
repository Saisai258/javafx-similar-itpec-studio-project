package application.student.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class Annou4Form {

	  public AnchorPane annou4Form() throws IOException {
		  AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/student/Annou4.fxml"));
		
			return root;
			
		}
}
