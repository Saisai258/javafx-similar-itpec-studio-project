package application.student.controller;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;

import application.student.form.*;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class BlogController {
    @FXML
    private VBox box1;
    @FXML
    private VBox box2;
    @FXML
    private VBox box3;
    @FXML
    private VBox box4;
    @FXML
    private VBox box5;
    @FXML
    private VBox box6;

    @FXML
    public void initialize() {
        animateSlideIn();
    }

    private void animateSlideIn() {
        List<VBox> boxes = Arrays.asList(box1, box2, box3, box4, box5, box6);
        double delay = 0; 

        for (VBox box : boxes) {
            if (box != null) {
                box.setTranslateX(500); // Start position (off-screen right)

                TranslateTransition transition = new TranslateTransition(Duration.millis(500), box);
                transition.setToX(0); // Move to original position
                transition.setDelay(Duration.millis(delay));
                transition.play();

                delay += 150; // Staggered effect
            }
        }
    }

    @FXML
    void blog1LinkAction(ActionEvent event) throws IOException {
        Blog1Form b1 = new Blog1Form();
        AnchorPane blog1Content = b1.blog1Form();
        applySlideInEffect(blog1Content, 300); // Slide in from right
        StuDashboardForm.root.setCenter(blog1Content);
    }

    @FXML
    void blog2LinkAction(ActionEvent event) throws IOException {
        Blog2Form b2 = new Blog2Form();
        AnchorPane blog2Content = b2.blog2Form();
        applySlideInEffect(blog2Content, 300); // Slide in from right
        StuDashboardForm.root.setCenter(blog2Content);
    }

    @FXML
    void blog3LinkAction(ActionEvent event) throws IOException {
        Blog3Form b3 = new Blog3Form();
        AnchorPane blog3Content = b3.blog3Form();
        applySlideInEffect(blog3Content, 300); // Slide in from right
        StuDashboardForm.root.setCenter(blog3Content);
    }

    @FXML
    void blog4LinkAction(ActionEvent event) throws IOException {
        Blog4Form b4 = new Blog4Form();
        AnchorPane blog4Content = b4.blog4Form();
        applySlideInEffect(blog4Content, 300); // Slide in from right
        StuDashboardForm.root.setCenter(blog4Content);
    }

    @FXML
    void blog5LinkAction(ActionEvent event) throws IOException {
        Blog5Form b5 = new Blog5Form();
        AnchorPane blog5Content = b5.blog5Form();
        applySlideInEffect(blog5Content, 300); // Slide in from right
        StuDashboardForm.root.setCenter(blog5Content);
    }

    @FXML
    void blog6LinkAction(ActionEvent event) throws IOException {
        Blog6Form b6 = new Blog6Form();
        AnchorPane blog6Content = b6.blog6Form();
        applySlideInEffect(blog6Content, 300); // Slide in from right
        StuDashboardForm.root.setCenter(blog6Content);
    }

    private void applySlideInEffect(AnchorPane content, double fromX) {
        content.setTranslateX(fromX); // Start position (off-screen)

        TranslateTransition slideTransition = new TranslateTransition(Duration.millis(300), content);
        slideTransition.setFromX(fromX); // Start from specified position
        slideTransition.setToX(0); // Move to normal position

        slideTransition.play();
    }
}
