package application.student.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class Annou2Form {

	  public AnchorPane annou2Form() throws IOException {
		  AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/student/Annou2.fxml"));
		
			return root;
			
		}
}
