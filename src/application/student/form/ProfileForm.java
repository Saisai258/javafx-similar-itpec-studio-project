package application.student.form;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProfileForm {
    public static Stage profileStage = null;
    private static boolean readyToClose = false;

    public void toggleProfileForm() throws IOException {
        if (profileStage == null || !profileStage.isShowing()) {
            openProfileForm();
        } else {
            closeForm();
        }
    }

    private void openProfileForm() throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/application/student/Profile.fxml"));
        Scene scene = new Scene(root, 290, 470);

        profileStage = new Stage();
        profileStage.setScene(scene);
        profileStage.getIcons().add(new Image("downLogo.jpg"));
        profileStage.initStyle(StageStyle.UNDECORATED);
        profileStage.setAlwaysOnTop(true);
        profileStage.setX(1052);
        profileStage.setY(127);

        profileStage.setOnHidden(e -> {
            profileStage = null;
            readyToClose = false;
        });

        // Delay show on JavaFX thread to prevent crash
        Platform.runLater(() -> {
            try {
                profileStage.show();
                // Mark safe to close after a brief delay
                Platform.runLater(() -> readyToClose = true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void closeForm() {
        if (profileStage != null && profileStage.isShowing()) {
            profileStage.close();
            profileStage = null;
        }
    }

    // Check if profile form can be safely closed (used by TeacherHomeForm)
    public static boolean canBeSafelyClosed() {
        return readyToClose;
    }
}
