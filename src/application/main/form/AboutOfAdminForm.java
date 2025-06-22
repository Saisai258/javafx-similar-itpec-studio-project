package application.main.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;

public class AboutOfAdminForm {

	public ScrollPane aboutOfAdminForm() throws IOException {
		ScrollPane aboutAdmin= (ScrollPane)FXMLLoader.load(getClass().getResource("/application/main/AboutOfAdmin.fxml"));
		
		return aboutAdmin;
		
	}
}
