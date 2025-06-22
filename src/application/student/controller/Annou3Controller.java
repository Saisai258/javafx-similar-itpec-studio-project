package application.student.controller;

import java.io.IOException;

import application.student.form.Annou2Form;
import application.student.form.Annou4Form;
import application.student.form.StuDashboardForm;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Annou3Controller {

    @FXML
    void nextLinkAction(ActionEvent event) throws IOException {
        Annou4Form a4 = new Annou4Form();
        AnchorPane annou4Content = a4.annou4Form();
        applySlideInEffect(annou4Content, 300); // Slide in from right
        StuDashboardForm.root.setCenter(annou4Content);
    }

    @FXML
    void preLinkAction(ActionEvent event) throws IOException {
        Annou2Form a2 = new Annou2Form();
        AnchorPane annou2Content = a2.annou2Form();
        applySlideInEffect(annou2Content, -150); // Slide in from left
        StuDashboardForm.root.setCenter(annou2Content);
    }

    private void applySlideInEffect(AnchorPane content, double fromX) {
        content.setTranslateX(fromX); // Start position (off-screen)

        TranslateTransition slideTransition = new TranslateTransition(Duration.millis(300), content);
        slideTransition.setFromX(fromX); // Start from specified position
        slideTransition.setToX(0); // Move to normal position

        slideTransition.play();
    }
}
