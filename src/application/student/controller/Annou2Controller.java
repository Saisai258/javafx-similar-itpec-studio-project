package application.student.controller;

import java.io.IOException;

import application.student.form.Annou1Form;
import application.student.form.Annou3Form;
import application.student.form.StuDashboardForm;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Annou2Controller {

    @FXML
    void nextLinkAction(ActionEvent event) throws IOException {
        Annou3Form a3 = new Annou3Form();
        AnchorPane annou3Content = a3.annou3Form();
        applySlideInEffect(annou3Content, 300); // Slide in from right
        StuDashboardForm.root.setCenter(annou3Content);
    }

    @FXML
    void preLinkAction(ActionEvent event) throws IOException {
        Annou1Form a1 = new Annou1Form();
        AnchorPane annou1Content = a1.annou1Form();
        applySlideInEffect(annou1Content, -150); // Slide in from left
        StuDashboardForm.root.setCenter(annou1Content);
    }

    private void applySlideInEffect(AnchorPane content, double fromX) {
        content.setTranslateX(fromX); // Start position (off-screen)

        TranslateTransition slideTransition = new TranslateTransition(Duration.millis(300), content);
        slideTransition.setFromX(fromX); // Start from specified position
        slideTransition.setToX(0); // Move to normal position

        slideTransition.play();
    }
}
