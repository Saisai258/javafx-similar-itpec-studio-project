package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class StudentResultsFEForm {

	public StudentResultsFEForm() {
		// TODO Auto-generated constructor stub
		
	}
	
	
			
	public  AnchorPane studentResultsFEForm() throws IOException {		
		System.out.println("fe results");
		AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/teacher/StudentResultsFE.fxml"));			
		return root;
		
	}

}
