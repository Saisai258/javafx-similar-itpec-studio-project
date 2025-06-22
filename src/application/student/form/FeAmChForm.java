package application.student.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class FeAmChForm {

	  public AnchorPane feAmChExamForm() throws IOException {
		  AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/student/FeAmChQuestionType.fxml"));
		
			return root;
			
		}
}
