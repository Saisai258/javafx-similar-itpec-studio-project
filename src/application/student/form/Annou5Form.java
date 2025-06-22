package application.student.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class Annou5Form {

	  public AnchorPane annou5Form() throws IOException {
		  AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/student/Annou5.fxml"));
		
			return root;
			
		}
}
