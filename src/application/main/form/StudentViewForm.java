package application.main.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
public class StudentViewForm {

	public ScrollPane studentViewForm() throws IOException {
		ScrollPane root= (ScrollPane)FXMLLoader.load(getClass().getResource("/application/main/StudentView1.fxml"));
	
		return root;
		
	}
}
