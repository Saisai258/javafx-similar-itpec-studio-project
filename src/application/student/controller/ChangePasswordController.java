package application.student.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.main.AppMain;
import application.main.DatabaseConnection;
import application.student.Student;
import application.student.form.ChangePasswordForm;
import application.student.form.LogOutForm;
import application.student.form.ProfileForm;
import application.student.form.StuDashboardForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class ChangePasswordController implements Initializable {

    @FXML
    private TextField txtid;

    @FXML
    private PasswordField txtOldPass;

    @FXML
    private PasswordField txtnewpassword;

    @FXML
    private PasswordField txtconfirmnewpassword;

    private String password = null;
    private String enteredPassword = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeStudentId();

        Tooltip.install(txtOldPass, new Tooltip("Enter your current password"));
        Tooltip.install(txtnewpassword, new Tooltip(
            "Password Requirements:\n" +
            "\u2714 At least one uppercase letter (A–Z)\n" +
            "\u2714 At least one lowercase letter (a–z)\n" +
            "\u2714 At least one digit (0–9)\n" +
            "\u2714 At least one special character (!@#$%^&*)\n" +
            "\u2714 Minimum 8 characters"
        ));
        Tooltip.install(txtconfirmnewpassword, new Tooltip("Re-enter the new password to confirm it matches"));
    }

    private void initializeStudentId() {
        Student logStu = SignInController.login_student;
        if (logStu != null) {
            txtid.setText(logStu.getStudent_id());
        }
    }

    @FXML
    void btnChangePasswordAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        initializeStudentId();

        if (!txtnewpassword.getText().equals(txtconfirmnewpassword.getText())) {
            showAlert(Alert.AlertType.WARNING, "Password Matching", "Password Mismatch!", "Please check your password fields.");
            return;
        }

        if (!dataCheck()) return;

        if (!validateInitialPasswordFields()) return;

        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.getConnetion();
        PreparedStatement pst = con.prepareStatement("UPDATE student SET password = ? WHERE student_id = ?");

        String studentId = txtid.getText();
        pst.setString(1, txtnewpassword.getText());
        pst.setString(2, studentId);

        int rowsAffected = pst.executeUpdate();
        clearFields();

        if (rowsAffected > 0) {
            showAlert(Alert.AlertType.INFORMATION, "Update Password", "Success!", "Password updated successfully.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Update Failed", "Failure!", "Password update was not successful.");
        }
     // Close LogOutForm if it's open
        if (LogOutForm.logOutStage != null && LogOutForm.logOutStage.isShowing()) {
            LogOutForm.logOutStage.close();
            LogOutForm.logOutStage = null;
        }

        // Close ProfileForm if it's open
        if (ProfileForm.profileStage != null && ProfileForm.profileStage.isShowing()) {
            ProfileForm.profileStage.close();
            ProfileForm.profileStage = null;
        }
        if (StuDashboardForm.sdfForm != null && StuDashboardForm.sdfForm .isShowing()) {
        	StuDashboardForm.sdfForm .close();
        	StuDashboardForm.sdfForm  = null;
        }
        AppMain.primaryStage.show(); 
    }

    private boolean dataCheck() throws SQLException {
        extractPasswordFromDB();
        enteredPassword = txtOldPass.getText();

        if (!password.equals(enteredPassword)) {
            showAlert(Alert.AlertType.ERROR, "Incorrect Password", "Old Password Mismatch!", "The entered old password is incorrect.");
            return false;
        }
        return true;
    }

    private void extractPasswordFromDB() throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.getConnetion();
        PreparedStatement pst = con.prepareStatement("SELECT password FROM student WHERE student_id = ?");
        pst.setString(1, txtid.getText());
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            password = rs.getString("password");
        }
    }

    private boolean validateInitialPasswordFields() {
        boolean valid = true;

        txtOldPass.setStyle(null);
        txtnewpassword.setStyle(null);
        txtconfirmnewpassword.setStyle(null);

        if (txtOldPass.getText().isEmpty() || !password.equals(enteredPassword)) {
            txtOldPass.setStyle("-fx-border-color: red;");
            valid = false;
        } else {
            txtOldPass.setStyle("-fx-border-color: green;");
        }

        if (txtnewpassword.getText().isEmpty() || txtnewpassword.getLength() < 8) {
            txtnewpassword.setStyle("-fx-border-color: red;");
            valid = false;
        } else {
            txtnewpassword.setStyle("-fx-border-color: green;");
        }

        if (txtconfirmnewpassword.getText().isEmpty() || !txtconfirmnewpassword.getText().equals(txtnewpassword.getText())) {
            txtconfirmnewpassword.setStyle("-fx-border-color: red;");
            valid = false;
        } else {
            txtconfirmnewpassword.setStyle("-fx-border-color: green;");
        }

        if (!valid) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "All fields are required!", "Please complete all fields.");
            return false;
        }

        if (!isPasswordValid(txtnewpassword.getText())) {
            showAlert(Alert.AlertType.ERROR, "Invalid Password", "Password doesn't meet required format", "Please follow the password rules shown in the tooltip or popup.");
            return false;
        }

        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$");
    }

    private void clearFields() {
        txtOldPass.clear();
        txtnewpassword.clear();
        txtconfirmnewpassword.clear();
        txtid.clear();
    }

    @FXML
    void cancelAction(ActionEvent event) {
        ChangePasswordForm.changePasswordForm.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}
