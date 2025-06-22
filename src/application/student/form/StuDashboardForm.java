package application.student.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StuDashboardForm {
    public static Stage sdfForm;
    public static BorderPane root;

    public Stage stuDashboardForm() throws IOException {
    	sdfForm = new Stage();
        root = (BorderPane) FXMLLoader.load(getClass().getResource("/application/student/StudentDashboard.fxml"));
        Scene sc = new Scene(root, 1366, 700);
        sdfForm.setMaximized(true);
        sdfForm.getIcons().add(new Image("downLogo.jpg"));
        sdfForm.setTitle("Quiz Mania Ultimate Challenge");
        sdfForm.setScene(sc);

        // Handle close
        sdfForm.setOnCloseRequest(event -> {
            ProfileForm.closeForm();
            UpdateProfileForm.closeForm();
        });

        // Avoid profile form being closed during opening
        sdfForm.iconifiedProperty().addListener((obs, wasMinimized, isNowMinimized) -> {
            if (isNowMinimized && ProfileForm.canBeSafelyClosed()) {
                ProfileForm.closeForm();
                UpdateProfileForm.closeForm();
            }
        });

        sdfForm.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && ProfileForm.canBeSafelyClosed()) {
                ProfileForm.closeForm();
                UpdateProfileForm.closeForm();
            }
        });

        return sdfForm;
    }
}
