package application.teacher.form;

	import java.io.IOException;

	import javafx.fxml.FXMLLoader;
	import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
	import javafx.stage.Stage;

	public class UpdateQuestionIpForm {
		public static Stage uqf;
		  public Stage updateQuestionForm2() throws IOException {
				
			uqf=new Stage();
			AnchorPane root=(AnchorPane)FXMLLoader.load(getClass().getResource("/application/teacher/UpdateQuestionIpForm.fxml"));
				Scene sc=new Scene(root,1366,700);
		        uqf.setMaximized(true);
	            uqf.getIcons().add(new Image("downLogo.jpg"));
				uqf.setScene(sc);
				uqf.setTitle("Quiz Mania Ultimate Challenge");
				
				return uqf;
			}
	}

