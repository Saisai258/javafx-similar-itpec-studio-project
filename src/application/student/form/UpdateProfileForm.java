package application.student.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UpdateProfileForm {

    private static Stage updForm = null;

    public void toggleUpdateProfileForm() throws IOException {
        if (updForm == null || !updForm.isShowing()) {
            openUpdateProfileForm();
        } else {
            closeForm();
        }
    }

    private void openUpdateProfileForm() throws IOException {
        updForm = new Stage();

        AnchorPane root = FXMLLoader.load(getClass().getResource("/application/student/UpdateProfile.fxml"));

        Scene scene = new Scene(root, 800, 500);
        updForm.setX(225);
        updForm.setY(127);
        updForm.getIcons().add(new Image("downLogo.jpg"));
        updForm.setScene(scene);
        updForm.setTitle("Update Profile");
        updForm.initStyle(StageStyle.UNDECORATED);
        updForm.show();
    }

    public static void closeForm() {
        if (updForm != null) {
            updForm.close();
            updForm = null;
        }
    }

    public static boolean isFormOpen() {
        return updForm != null && updForm.isShowing();
    }
}
