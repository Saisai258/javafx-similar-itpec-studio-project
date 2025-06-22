package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class QuestionForm {
	public static Stage questionsForm;
	public Stage questionForm() throws IOException {
		
		questionsForm=new Stage();
			AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/teacher/Question1.fxml"));
			Scene sc=new Scene(root,1366,700);
	        questionsForm.getIcons().add(new Image("downLogo.jpg"));
			questionsForm.setScene(sc);
			questionsForm.setMaximized(true);
			questionsForm.setTitle("Quiz Mania Ultimate Challenge");
			
			return questionsForm;
		}
}
