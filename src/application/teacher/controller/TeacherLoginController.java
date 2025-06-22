package application.teacher.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.main.DatabaseConnection;
import application.main.AppMain;
import application.teacher.Teacher;
import application.teacher.form.ForgotPasswordForm;
import application.teacher.form.TeacherHomeForm;
import application.teacher.form.TeacherLoginForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class TeacherLoginController {
    public static Teacher login_teacher;

    @FXML
    private TextField txtid;

    @FXML
    private PasswordField txtpwd;
  
    @FXML
    void loginaction(ActionEvent event) throws SQLException, IOException {
        String idInput = txtid.getText().trim();
        String pwdInput = txtpwd.getText().trim();

        if (idInput.isEmpty() || pwdInput.isEmpty()) {
            showAlert(AlertType.WARNING, "Required Data", "Fill all required data!", "Please fill all required fields.");
            return;
        }

        if (!isValidId(idInput)) {
            showAlert(AlertType.ERROR, "Invalid ID", "ID format error", "Teacher ID must end with numbers after '-' (e.g., MB-0000000000).");
            return;
        }
        if(getActionNum(idInput)==0) {
        	showAlert(AlertType.WARNING, "Login Fill", "Your Account is Invalid!", "Please contact system Administrator.");
        	return;
        }

        DatabaseConnection db = new DatabaseConnection();
        try (Connection con = db.getConnetion()) {
            String sql = "SELECT * FROM teacher WHERE BINARY teacher_id=? AND password=?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, idInput);
                pst.setString(2, pwdInput);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        // Retrieve teacher details
                        String trid = rs.getString("teacher_id");
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        String password = rs.getString("password");
                        String address = rs.getString("address");
                        String phone = rs.getString("phone");
                        String gender = rs.getString("gender");
                        String specialism = rs.getString("specialism");
                        Date dob = rs.getDate("date_of_birth");
                        byte[] photo = rs.getBytes("photo");
                        int size = rs.getInt("size");

                        login_teacher = new Teacher(trid, name, email, password, address, phone, gender, specialism, dob, photo, size);
                        
                        new TeacherLoginForm().toggleTeacherLoginForm();
                        AppMain.primaryStage.close();
                        TeacherHomeForm trd = new TeacherHomeForm();
                        trd.teacherHomeForm().show();
                    } else {
                        showAlert(AlertType.ERROR, "Login Failed", null, "Invalid teacher ID or password.");
                        System.out.println("Login unsuccessful.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", null, "An error occurred while connecting to the database.");
        }
    }

    private int getActionNum(String id) throws SQLException {
    	 DatabaseConnection db = new DatabaseConnection();
    	 Connection con = db.getConnetion();
    	 String query="select action from teacherinformation where teach_id=?";
    	 PreparedStatement pst = con.prepareStatement(query);
    	 pst.setString(1, id);
    	 ResultSet rs = pst.executeQuery();
    	 if(rs.next()) {
    		 return 1;
    	 }
		return 0;
    	
    }
    public boolean isValidId(String id) {
        int dashIndex = id.lastIndexOf("-");
        if (dashIndex != -1 && dashIndex < id.length() - 1) {
            String numberPart = id.substring(dashIndex + 1);
            return numberPart.matches("\\d+");
        }
        return false;
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
    @FXML
    void forgetPwdAction(ActionEvent event) throws IOException {
    	ForgotPasswordForm f = new ForgotPasswordForm();
    			f.forgotPasswordForm().show();
    }
}
