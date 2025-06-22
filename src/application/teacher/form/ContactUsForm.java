package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class ContactUsForm {

	  public AnchorPane contactUsForm() throws IOException {
		  AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/teacher/ContactUs.fxml"));
		
			return root;
			
		}
}
