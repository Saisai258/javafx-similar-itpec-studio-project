package application.main.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class AboutOfTeacherForm {

	public AnchorPane aboutOfTeacherForm() throws IOException {
		AnchorPane aboutTeacher= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/main/AboutOfTeacher.fxml"));

		return aboutTeacher;
		
	}
}
