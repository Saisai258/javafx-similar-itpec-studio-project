package application.admin.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class StudentResultsIPForm {
	public  AnchorPane studentResultsIPForm() throws IOException {	
		AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/admin/StudentResultsIP.fxml"));			
		return root;
		
	}

}
