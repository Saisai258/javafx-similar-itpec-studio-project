package application.admin.form;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
public class UserManagementStudentForm {	
	public AnchorPane userManagementStudentForm () throws IOException{
	    AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/admin/UserManagementStudent.fxml"));
	    
		return root;
		
	  }
	
}

