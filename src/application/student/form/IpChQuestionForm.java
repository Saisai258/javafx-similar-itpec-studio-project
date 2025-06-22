package application.student.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class IpChQuestionForm {

	  public AnchorPane ipChQuestionForm() throws IOException {
		  AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/student/ipChQuestionType.fxml"));
		
			return root;
			
		}
}