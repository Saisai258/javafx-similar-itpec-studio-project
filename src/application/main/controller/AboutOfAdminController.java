package application.main.controller;

import java.io.IOException;
import application.admin.form.SignInForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AboutOfAdminController {

	
	  @FXML
	    void btnAdmin(ActionEvent event) throws IOException {
		 new SignInForm().toggleSignInForm();		  
	    }
}
