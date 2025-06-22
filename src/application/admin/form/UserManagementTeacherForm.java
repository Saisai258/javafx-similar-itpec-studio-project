package application.admin.form;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
public class UserManagementTeacherForm {

public AnchorPane userManagementTeacherForm () throws IOException{
	AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/admin/UserManagementTeacher.fxml"));
	
    return root;
  }
}
