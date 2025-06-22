package application.admin.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.admin.Admin;
import application.admin.form.ForgotPasswordForm;
import application.admin.form.HomeForm1;
import application.main.DatabaseConnection;
import application.main.AppMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignInController {
	public static Admin login_admin;

	public static String id;
	public static String pass;

//	public static String password;

	@FXML
	private TextField txtID;

	@FXML
	private PasswordField txtpwd;

	@FXML
	public void handleSignIn() throws IOException {
	String inputId = txtID.getText().trim();
	String inputPw = txtpwd.getText().trim();

	if (inputId.isEmpty() || inputPw.isEmpty()) {
		showAlert(Alert.AlertType.INFORMATION, "Required Data", "Missing Information",
				"Please fill in both Admin ID and Password.");
		return;
	}

	id = inputId;
	pass = inputPw;

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	try {
		DatabaseConnection db = new DatabaseConnection();
		con = db.getConnetion();

		// Step 1: Check if admin ID exists
		pst = con.prepareStatement("SELECT * FROM admin WHERE BINARY admin_id = ?");
		pst.setString(1, inputId);
		rs = pst.executeQuery();

		if (!rs.next()) {
			showAlert(Alert.AlertType.ERROR, "Login Failed", "Admin ID Not Found",
					"The entered Admin ID does not exist.");
			return;
		}

		// Step 2: If ID exists, check password
		pst.close();
		rs.close();
		pst = con.prepareStatement("SELECT * FROM admin WHERE BINARY admin_id = ? AND password = ?");
		pst.setString(1, inputId);
		pst.setString(2, inputPw);
		rs = pst.executeQuery();

		if (rs.next()) {
			String admin_id = rs.getString("admin_id");
			String name = rs.getString("name");
			String email = rs.getString("email");
			String password = rs.getString("password");
			String address = rs.getString("address");
			String phone = rs.getString("phone");
			String gender = rs.getString("gender");
			Date DOB = rs.getDate("date_of_birth");

			login_admin = new Admin(admin_id, name, email, password, address, phone, gender, DOB);

			// Show admin dashboard
			new HomeForm1().adminForm().show();
			AppMain.primaryStage.hide();
		} else {
			showAlert(Alert.AlertType.WARNING, "Login Failed", "Incorrect Password",
					"The password you entered is incorrect.");
		}

	} catch (SQLException e) {
		e.printStackTrace();
		showAlert(Alert.AlertType.ERROR, "Database Error", "Connection Failed",
				"Could not connect to the database.");
	} finally {
		try {
			if (rs != null) rs.close();
			if (pst != null) pst.close();
			if (con != null) con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

	@FXML
	void handleStudentMouseAction(ActionEvent event) {
		// Placeholder for future functionality
	}

	private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}

	@FXML
	void ForgotPassLinkAction(ActionEvent event) throws IOException {
		ForgotPasswordForm forgot = new ForgotPasswordForm();
		forgot.forgotPasswordForm().show();
	}
}
