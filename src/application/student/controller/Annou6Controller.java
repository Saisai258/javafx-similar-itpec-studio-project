package application.student.controller;

import java.io.IOException;

import application.student.form.Annou1Form;
import application.student.form.Annou5Form;
import application.student.form.StuDashboardForm;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Annou6Controller {

    @FXML
    void nextLinkAction(ActionEvent event) throws IOException {
        Annou1Form a1 = new Annou1Form();
        AnchorPane annou1Content = a1.annou1Form();
        applySlideInEffect(annou1Content, 300); // Slide in from right
        StuDashboardForm.root.setCenter(annou1Content);
    }

    @FXML
    void preLinkAction(ActionEvent event) throws IOException {
        Annou5Form a5 = new Annou5Form();
        AnchorPane annou5Content = a5.annou5Form();
        applySlideInEffect(annou5Content, -150); // Slide in from left
        StuDashboardForm.root.setCenter(annou5Content);
    }

    private void applySlideInEffect(AnchorPane content, double fromX) {
        content.setTranslateX(fromX); // Start position (off-screen)

        TranslateTransition slideTransition = new TranslateTransition(Duration.millis(300), content);
        slideTransition.setFromX(fromX); // Start from specified position
        slideTransition.setToX(0); // Move to normal position

        slideTransition.play();
    }
}
