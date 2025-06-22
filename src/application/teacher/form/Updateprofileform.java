package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Updateprofileform {
	public static Stage uqf;
	  public Stage updateprofileform() throws IOException {
			
		uqf=new Stage();
	    AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/teacher/TeacherUpdateProfile.fxml"));
	    Scene sc = new Scene(root, 800, 500);
        uqf.getIcons().add(new Image("downLogo.jpg"));
        uqf.setScene(sc);
        uqf.initStyle(StageStyle.UNDECORATED);

        uqf.setX(225);
        uqf.setY(127);
        return uqf;
		}
	  public static void closeForm() {
	        if (uqf != null && uqf.isShowing()) {
	        	uqf.close();
	        	uqf = null;
	        }
	    }
}
