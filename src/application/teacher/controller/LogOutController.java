package application.teacher.controller;

import java.io.IOException;

import application.teacher.form.LogOutForm;
import application.teacher.form.ProfileForm;
import application.teacher.form.TeacherHomeForm;
import application.main.AppMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LogOutController {

    @FXML
    void btnlogoutAction(ActionEvent event) throws IOException {
    	// Close LogOutForm if it's open
        if (LogOutForm.logOutStage != null && LogOutForm.logOutStage.isShowing()) {
            LogOutForm.logOutStage.close();
            LogOutForm.logOutStage = null;
        }

        // Close ProfileForm if it's open
        if (ProfileForm.profileStage != null && ProfileForm.profileStage.isShowing()) {
            ProfileForm.profileStage.close();
            ProfileForm.profileStage = null;
        }
        if (TeacherHomeForm.homeForm != null && TeacherHomeForm.homeForm.isShowing()) {
        	TeacherHomeForm.homeForm.close();
        	TeacherHomeForm.homeForm = null;
        }
        AppMain.primaryStage.show(); // mainview form hide
    }

    @FXML
    void btnlogoutcancelAction(ActionEvent event) {
    	if (LogOutForm.logOutStage != null) {
	        LogOutForm.logOutStage.close();
	        LogOutForm.logOutStage = null;  // Reset the reference
	    }
    }

}
