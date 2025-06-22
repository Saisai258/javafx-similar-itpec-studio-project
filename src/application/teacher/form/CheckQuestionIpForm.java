package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class CheckQuestionIpForm {
	
	  public Pane checkQuestionForm() throws IOException {
			Pane root= (Pane)FXMLLoader.load(getClass().getResource("/application/teacher/CheckQuestionIp.fxml"));
		
			return root;
			
		}
}