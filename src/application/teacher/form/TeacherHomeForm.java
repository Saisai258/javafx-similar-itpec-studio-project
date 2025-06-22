package application.teacher.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TeacherHomeForm {
    public static Stage homeForm;
    public static BorderPane root;

    public Stage teacherHomeForm() throws IOException {
        homeForm = new Stage();
        root = (BorderPane) FXMLLoader.load(getClass().getResource("/application/teacher/TeacherHome.fxml"));
        Scene sc = new Scene(root, 1366, 700);
        homeForm.setMaximized(true);
        homeForm.getIcons().add(new Image("downLogo.jpg"));
        homeForm.setTitle("Quiz Mania Ultimate Challenge");
        homeForm.setScene(sc);

        // Handle close
        homeForm.setOnCloseRequest(event -> {
            ProfileForm.closeForm();
            Updateprofileform.closeForm();
        });

        // Avoid profile form being closed during opening
        homeForm.iconifiedProperty().addListener((obs, wasMinimized, isNowMinimized) -> {
            if (isNowMinimized && ProfileForm.canBeSafelyClosed()) {
                ProfileForm.closeForm();
                Updateprofileform.closeForm();
            }
        });

        homeForm.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && ProfileForm.canBeSafelyClosed()) {
                ProfileForm.closeForm();
                Updateprofileform.closeForm();
            }
        });

        return homeForm;
    }
}
