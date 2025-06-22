package application.student.controller;

import java.io.IOException;

import application.student.form.FeAmChForm;
import application.student.form.MaintenanceForm;
import application.student.form.StuDashboardForm;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class FeExamController {
	
	    @FXML
	    void amChLinkAction(ActionEvent event) throws IOException {
	        FeAmChForm fe = new FeAmChForm();
	        AnchorPane examContent = fe.feAmChExamForm();
	        applyZoomInFadeInEffect(examContent);
	        StuDashboardForm.root.setCenter(examContent);
	    }
	    
	    @FXML
	    void pmChLinkAction(ActionEvent event) throws IOException {
	        MaintenanceForm mt = new MaintenanceForm();
	        AnchorPane examContent = mt.maintenanceForm();
	        applyZoomInFadeInEffect(examContent);
	        StuDashboardForm.root.setCenter(examContent);
	    }

	    private void applyZoomInFadeInEffect(AnchorPane content) {
	        // Initially scale and make transparent
	        content.setScaleX(0.5);
	        content.setScaleY(0.5);
	        content.setOpacity(0); // Initially invisible

	        // Create a ScaleTransition for zoom-in effect
	        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), content);
	        scaleTransition.setFromX(0.5);
	        scaleTransition.setFromY(0.5);
	        scaleTransition.setToX(1.0);
	        scaleTransition.setToY(1.0);

	        // Create a FadeTransition for fade-in effect
	        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), content);
	        fadeTransition.setFromValue(0.0); // Start fully transparent
	        fadeTransition.setToValue(1.0);   // Fully visible

	        // Play both transitions together
	        scaleTransition.play();
	        fadeTransition.play();
	    }

}
