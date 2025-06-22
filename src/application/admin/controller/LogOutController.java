package application.admin.controller;

import java.io.IOException;

import application.admin.form.HomeForm1;
import application.admin.form.LogOutForm;
import application.admin.form.ProfileForm;
import application.admin.form.RegisterChoiceForm;
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

        // Close RegisterChoiceForm if it's open
        if (RegisterChoiceForm.rForm != null && RegisterChoiceForm.rForm.isShowing()) {
            RegisterChoiceForm.rForm.close();
            RegisterChoiceForm.rForm = null;
        }
        
        if(HomeForm1.homeStage !=null && HomeForm1.homeStage.isShowing()) {
        	HomeForm1.homeStage.close();
        	HomeForm1.homeStage=null;
        }
//        // Check if MainViewsForm.mainForm is initialized
//        if (MainViewsForm.mainForm == null) {
//            MainViewsForm mainView = new MainViewsForm();
//            MainViewsForm.mainForm = mainView.mainViewsForm(); // Create new Stage
//        }

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
