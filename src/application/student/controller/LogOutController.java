package application.student.controller;

import java.io.IOException;

import application.main.AppMain;
import application.student.form.LogOutForm;
import application.student.form.ProfileForm;
import application.student.form.StuDashboardForm;
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
        if (StuDashboardForm.sdfForm != null && StuDashboardForm.sdfForm .isShowing()) {
        	StuDashboardForm.sdfForm .close();
        	StuDashboardForm.sdfForm  = null;
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
