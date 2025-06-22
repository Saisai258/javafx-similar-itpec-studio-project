package application.student.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FePmChForm {
	
	public static Stage fePmChForm;

    public Stage fePmChExamForm() throws IOException {
        if (fePmChForm == null) { // Check if rForm is already initialized
            fePmChForm = new Stage();
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/application/student/FePmChQuestionType.fxml"));
            Scene sc = new Scene(root, 1366,700);
            fePmChForm.getIcons().add(new Image("downLogo.jpg"));
            fePmChForm.setScene(sc);
            fePmChForm.setTitle("FeExam Form");
        }
        return fePmChForm;
    }
}