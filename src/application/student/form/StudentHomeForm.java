package application.student.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class StudentHomeForm {

	  public AnchorPane homeForm() throws IOException {
		  AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/student/StudentHome.fxml"));
		
			return root;
			
		}
}
