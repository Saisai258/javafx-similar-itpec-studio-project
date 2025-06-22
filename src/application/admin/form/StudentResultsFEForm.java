package application.admin.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class StudentResultsFEForm {	
	public  AnchorPane studentResultsFEForm() throws IOException {		
		AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/admin/StudentResultsFE.fxml"));			
		return root;
		
	}

}
