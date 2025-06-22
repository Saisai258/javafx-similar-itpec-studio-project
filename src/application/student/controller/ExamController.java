package application.student.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import application.student.form.FeExamForm;
import application.student.form.IpExamForm;
import application.student.form.MaintenanceForm;
import application.student.form.StuDashboardForm;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ExamController implements Initializable {

	public static String ExamName = new String();

	@FXML
	private VBox annouBox1;
	@FXML
	private VBox annouBox2;
	@FXML
	private VBox annouBox3;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		slideInFromBottom();
	}

	private void slideInFromBottom() {
		List<VBox> boxes = Arrays.asList(annouBox1, annouBox2, annouBox3);
		double delay = 0;

		for (VBox box : boxes) {
			if (box != null) {
				box.setTranslateY(100); // Start position (off-screen bottom)

				TranslateTransition transition = new TranslateTransition(Duration.millis(600), box);
				transition.setToY(0); // Move to original position
				transition.setDelay(Duration.millis(delay));
				transition.play();

				delay += 200; // Staggered effect for smooth appearance
			}
		}
	}

	@FXML
	void shLinkAction(ActionEvent event) throws IOException {
		MaintenanceForm mt = new MaintenanceForm();
		AnchorPane examContent = mt.maintenanceForm();
		applyZoomInFadeInEffect(examContent);
		StuDashboardForm.root.setCenter(examContent);
	}

	@FXML
	void FeExamLinkAction(ActionEvent event) throws IOException {
		ExamName = "FE"; // Set the exam name
		FeExamForm mt = new FeExamForm();
		AnchorPane examContent = mt.feExamForm();
		applyZoomInFadeInEffect(examContent);
		StuDashboardForm.root.setCenter(examContent);
	}

	@FXML
	void IpExamLinkAction(ActionEvent event) throws IOException {
		ExamName = "IP"; // Set the exam name
		IpExamForm ip = new IpExamForm();
		AnchorPane examContent = ip.ipExamForm();
		applyZoomInFadeInEffect(examContent);
		StuDashboardForm.root.setCenter(examContent);
	}

	private void applyZoomInFadeInEffect(AnchorPane content) {
		// Initially scale the content down and make it fully transparent
		content.setScaleX(0.5);
		content.setScaleY(0.5);
		content.setOpacity(0);

		// Create a ScaleTransition to animate the zoom-in effect
		ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), content);
		scaleTransition.setFromX(0.5);
		scaleTransition.setFromY(0.5);
		scaleTransition.setToX(1.0);
		scaleTransition.setToY(1.0);

		// Create a FadeTransition to animate the fade-in effect
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), content);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);

		// Play both animations together
		scaleTransition.play();
		fadeTransition.play();
	}
}
