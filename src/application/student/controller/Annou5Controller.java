package application.student.controller;

import java.io.IOException;

import application.student.form.Annou4Form;
import application.student.form.Annou6Form;
import application.student.form.StuDashboardForm;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Annou5Controller {

    @FXML
    void nextLinkAction(ActionEvent event) throws IOException {
        Annou6Form a6 = new Annou6Form();
        AnchorPane annou6Content = a6.annou6Form();
        applySlideInEffect(annou6Content, 300); // Slide in from right
        StuDashboardForm.root.setCenter(annou6Content);
    }

    @FXML
    void preLinkAction(ActionEvent event) throws IOException {
        Annou4Form a4 = new Annou4Form();
        AnchorPane annou4Content = a4.annou4Form();
        applySlideInEffect(annou4Content, -150); // Slide in from left
        StuDashboardForm.root.setCenter(annou4Content);
    }

    private void applySlideInEffect(AnchorPane content, double fromX) {
        content.setTranslateX(fromX); // Start position (off-screen)

        TranslateTransition slideTransition = new TranslateTransition(Duration.millis(300), content);
        slideTransition.setFromX(fromX); // Start from specified position
        slideTransition.setToX(0); // Move to normal position

        slideTransition.play();
    }
}
