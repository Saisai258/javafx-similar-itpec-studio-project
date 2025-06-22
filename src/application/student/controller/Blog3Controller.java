package application.student.controller;

import java.io.IOException;

import application.student.form.Blog2Form;
import application.student.form.Blog4Form;
import application.student.form.StuDashboardForm;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Blog3Controller {

    @FXML
    void nextLinkAction(ActionEvent event) throws IOException {
        Blog4Form b4 = new Blog4Form();
        AnchorPane blog4Content = b4.blog4Form();
        applySlideInEffect(blog4Content, 300); // Slide in from right
        StuDashboardForm.root.setCenter(blog4Content);
    }

    @FXML
    void preLinkAction(ActionEvent event) throws IOException {
        Blog2Form b2 = new Blog2Form();
        AnchorPane blog2Content = b2.blog2Form();
        applySlideInEffect(blog2Content, -150); // Slide in from left
        StuDashboardForm.root.setCenter(blog2Content);
    }

    private void applySlideInEffect(AnchorPane content, double fromX) {
        content.setTranslateX(fromX); // Start position (off-screen)

        TranslateTransition slideTransition = new TranslateTransition(Duration.millis(300), content);
        slideTransition.setFromX(fromX); // Start from specified position
        slideTransition.setToX(0); // Move to normal position

        slideTransition.play();
    }
}
