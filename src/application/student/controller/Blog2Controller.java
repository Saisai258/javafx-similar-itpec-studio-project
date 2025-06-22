package application.student.controller;

import java.io.IOException;

import application.student.form.Blog1Form;
import application.student.form.Blog3Form;
import application.student.form.StuDashboardForm;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Blog2Controller {

    @FXML
    void nextLinkAction(ActionEvent event) throws IOException {
        Blog3Form b3 = new Blog3Form();
        AnchorPane blog3Content = b3.blog3Form();
        applySlideInEffect(blog3Content, 300); // Slide in from right
        StuDashboardForm.root.setCenter(blog3Content);
    }

    @FXML
    void preLinkAction(ActionEvent event) throws IOException {
        Blog1Form b1 = new Blog1Form();
        AnchorPane blog1Content = b1.blog1Form();
        applySlideInEffect(blog1Content, -150); // Slide in from left
        StuDashboardForm.root.setCenter(blog1Content);
    }

    private void applySlideInEffect(AnchorPane content, double fromX) {
        content.setTranslateX(fromX); // Start position (off-screen)

        TranslateTransition slideTransition = new TranslateTransition(Duration.millis(300), content);
        slideTransition.setFromX(fromX); // Start from specified position
        slideTransition.setToX(0); // Move to normal position

        slideTransition.play();
    }
}
