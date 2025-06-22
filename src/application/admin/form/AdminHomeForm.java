package application.admin.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class AdminHomeForm {

	public AnchorPane adminHome () throws IOException{
		AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/admin/AdminHome.fxml"));

		return root;		
	  }
}
