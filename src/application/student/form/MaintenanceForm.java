package application.student.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class MaintenanceForm {

	  public AnchorPane maintenanceForm() throws IOException {
		  AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/student/Maintenance.fxml"));
		
			return root;
			
		}
}
