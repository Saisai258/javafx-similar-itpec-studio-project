package application.student.controller;

import java.io.IOException;

import application.student.form.Blog1Form;
import application.student.form.Blog5Form;
import application.student.form.StuDashboardForm;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Blog6Controller {

    @FXML
    void nextLinkAction(ActionEvent event) throws IOException {
    	 Blog1Form b1 = new Blog1Form();
         AnchorPane blog1Content = b1.blog1Form();
         applySlideInEffect(blog1Content, 300); // Slide in from left
         StuDashboardForm.root.setCenter(blog1Content);
        }

    @FXML
    void preLinkAction(ActionEvent event) throws IOException {
        Blog5Form b5 = new Blog5Form();
        AnchorPane blog5Content = b5.blog5Form();
        applySlideInEffect(blog5Content, -150); // Slide in from left
        StuDashboardForm.root.setCenter(blog5Content);
    }

    private void applySlideInEffect(AnchorPane content, double fromX) {
        content.setTranslateX(fromX); // Start position (off-screen)

        TranslateTransition slideTransition = new TranslateTransition(Duration.millis(300), content);
        slideTransition.setFromX(fromX); // Start from specified position
        slideTransition.setToX(0); // Move to normal position

        slideTransition.play();
    }
}
