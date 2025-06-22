package application.student.form;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class StudentMainForm  {
	public static Stage mStage;
	
	public void showMainForm(){
		try {
			mStage = new Stage();
			AnchorPane root = (AnchorPane) FXMLLoader
					.load(getClass().getResource("/application/student/StudentMainView.fxml"));

			Scene scene = new Scene(root, 1366, 700);
            mStage.setResizable(false);
            mStage.getIcons().add(new Image("downLogo.jpg"));
			mStage.setScene(scene);
			mStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
