package application.admin.controller;

import java.io.IOException;

import application.admin.form.RegisterChoiceForm;
import application.admin.form.RegisterationForm;
import application.teacher.form.TeacherRegisterationform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AdminRegisterChoiceController {

    @FXML
    void btnadmin(ActionEvent event) throws IOException {
    	RegisterChoiceForm.closeForm();
    	TeacherRegisterationform.closeForm();
    	new RegisterationForm().toggleRegisterationForm();
    }

    @FXML
    void btnteacher(ActionEvent event) throws IOException {
    	RegisterChoiceForm.closeForm();
    	RegisterationForm.closeForm();
    	new TeacherRegisterationform().toggleTeacherRegistrationForm();
    }

}
