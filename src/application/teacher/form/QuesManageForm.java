package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class QuesManageForm {
	public Pane quesManageForm() throws IOException {
		Pane root= (Pane)FXMLLoader.load(getClass().getResource("/application/teacher/QuesManage.fxml"));
	
		return root;
		
	}
}
