package application.student.form;

import java.io.IOException;

import application.student.controller.ForgotPasswordController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ForgetForm {
	 public static Stage fForm;
	    public Stage forgetForm(String password) throws IOException {
	        if (fForm == null) { // Check if rForm is already initialized
	        	fForm = new Stage();
	        	 // Create a new BorderPane for displaying the password
                BorderPane newPane = new BorderPane();

                Label label1 = new Label("Your Password is: ");
                label1.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                Label label2 = new Label(password);
                label2.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");


                label2.setText(ForgotPasswordController.password);

                // Using HBox to store both labels
                HBox hbox = new HBox(10); // 10 is spacing between nodes
                hbox.setSpacing(20);
                hbox.setPadding(new Insets(20));
                hbox.getChildren().addAll(label1, label2);

                // Using VBox as the top container
                VBox topContainer = new VBox();
                topContainer.getChildren().add(hbox);
                newPane.setTop(topContainer);
                
	            Scene sc = new Scene(newPane, 300,200);
	            fForm.getIcons().add(new Image("downLogo.jpg"));
	            fForm.setScene(sc);
	            fForm.initStyle(StageStyle.UTILITY);
	        }
	        return fForm;
	    }
}
