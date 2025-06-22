package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class QuestionFEForm {
	public static Stage questionFeForm;
	public Stage questionFEForm() throws IOException {
		questionFeForm =new Stage();
		AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/teacher/QuestionFE.fxml"));
		Scene sc=new Scene(root,1366,700);
		questionFeForm.setMaximized(true);
        questionFeForm.getIcons().add(new Image("downLogo.jpg"));
        questionFeForm.setTitle("Quiz Mania Ultimate Challenge");
		questionFeForm.setScene(sc);
		
		return questionFeForm;		
	}	
}
