package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class QuestionLookIpForm {
	public static Stage form;
	public Stage questionLookForm() throws IOException {
	    form=new Stage();
	    AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/teacher/QuestionlookIp.fxml"));
	    Scene sc=new Scene(root,1366,700);
        form.setMaximized(true);
        form.getIcons().add(new Image("downLogo.jpg"));
	    form.setScene(sc);
	    form.setTitle("Quiz Mania Ultimate Challenge");
	    
	    return form;
	    
	  }
}
