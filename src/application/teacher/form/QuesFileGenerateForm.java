package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class QuesFileGenerateForm {

	  public AnchorPane quesFileGenerateForm() throws IOException {
		  AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/teacher/QuestonFileGenerate.fxml"));
		
			return root;
			
		}
}