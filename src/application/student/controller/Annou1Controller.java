package application.student.controller;

import java.io.IOException;

import application.student.form.Annou6Form;
import application.student.form.Annou2Form;
import application.student.form.StuDashboardForm;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Annou1Controller {

    @FXML
    void nextLinkAction(ActionEvent event) throws IOException {
        Annou2Form a2 = new Annou2Form();
        AnchorPane annou2Content = a2.annou2Form();
        applySlideInEffect(annou2Content, 300); // Slide in from right
        StuDashboardForm.root.setCenter(annou2Content);
    }

    @FXML
    void preLinkAction(ActionEvent event) throws IOException {
        Annou6Form a6 = new Annou6Form();
        AnchorPane annou6Content = a6.annou6Form();
        applySlideInEffect(annou6Content, -150); // Slide in from left
        StuDashboardForm.root.setCenter(annou6Content);
    }

    private void applySlideInEffect(AnchorPane content, double fromX) {
        content.setTranslateX(fromX); // Start position (off-screen)

        TranslateTransition slideTransition = new TranslateTransition(Duration.millis(300), content);
        slideTransition.setFromX(fromX); // Start from specified position
        slideTransition.setToX(0); // Move to normal position

        slideTransition.play();
    }
}
