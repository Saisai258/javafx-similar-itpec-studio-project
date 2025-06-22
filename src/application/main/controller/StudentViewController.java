package application.main.controller;

import java.io.IOException;

import application.student.form.StudentSignInForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class StudentViewController {

	
	 @FXML
	    void btnStudent(ActionEvent event) throws IOException {
		 new StudentSignInForm().toggleStudentSignInForm();
	    }
}
