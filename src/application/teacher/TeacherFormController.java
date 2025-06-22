package application.teacher;
import java.io.IOException;

import application.teacher.form.TeacherRegisterationform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class TeacherFormController {
    @FXML
    public void btnAdminAction(ActionEvent event) {
        System.out.println("Admin button clicked");
    }
    @FXML
    public void btnTeacherAction(ActionEvent event) throws IOException {
        System.out.println("Teacher button clicked");
    	new TeacherRegisterationform().toggleTeacherRegistrationForm();
		   
    }

    @FXML
    public void btnStudentAction(ActionEvent event) {
        System.out.println("Student button clicked");
    }
}
