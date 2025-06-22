package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class StudentResultsIPForm {

	public StudentResultsIPForm() {
		// TODO Auto-generated constructor stub
		
	}
	
	
			
	public  AnchorPane studentResultsIPForm() throws IOException {	
		System.out.println("ip form");
		AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/teacher/StudentResultsIP.fxml"));			
		return root;
		
	}

}
