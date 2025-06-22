package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class HomeForm {

	  public Pane homeForm() throws IOException {
			Pane root= (Pane)FXMLLoader.load(getClass().getResource("/application/teacher/Home.fxml"));
		
			return root;
			
		}
}
