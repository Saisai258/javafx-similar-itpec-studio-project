package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class QuestionLookFeForm {
	public static Stage form;
	public Stage questionLookForm() throws IOException {
	    form=new Stage();
	    AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/teacher/QuestionlookFe.fxml"));
	    Scene sc=new Scene(root,1366,700);
        form.getIcons().add(new Image("downLogo.jpg"));
        form.setTitle("Quiz Mania Ultimate Challenge");
	    form.setScene(sc);
        form.setMaximized(true);
	    return form;
	    
	  }
}
