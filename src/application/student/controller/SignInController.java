package application.student.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.main.DatabaseConnection;
import application.main.AppMain;
import application.student.Student;
import application.student.form.ForgotPasswordForm;
import application.student.form.StuDashboardForm;
import application.student.form.StudentRegisterationForm;
import application.student.form.StudentSignInForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignInController {

	public static Student login_student;

	@FXML
	private TextField txtSid;

	@FXML
	private PasswordField txtPw;

	@FXML
	public void handleSignIn() throws SQLException, ClassNotFoundException, IOException {
		String inputId = txtSid.getText().trim();
		String inputPw = txtPw.getText();

		if (inputId.isEmpty() || inputPw.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Required Data");
			alert.setHeaderText("Missing Information");
			alert.setContentText("Please fill in both Student ID and Password.");
			alert.show();
			return;
		}

		// Database connection
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		// First: Check if student ID exists
		PreparedStatement idCheck = con.prepareStatement("SELECT * FROM student WHERE BINARY student_id = ?");
		idCheck.setString(1, inputId);
		ResultSet idResult = idCheck.executeQuery();

		if (!idResult.next()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Login Failed");
			alert.setHeaderText("Student ID Not Found");
			alert.setContentText("The entered Student ID does not exist.");
			alert.show();
			return;
		}

		// Second: Check if password is correct
		PreparedStatement loginCheck = con
				.prepareStatement("SELECT * FROM student WHERE BINARY student_id = ? AND password = ?");
		loginCheck.setString(1, inputId);
		loginCheck.setString(2, inputPw);
		ResultSet rs = loginCheck.executeQuery();

		if (rs.next()) {
			int id = rs.getInt(1);
			String student_id = rs.getString(2);
			String name = rs.getString(3);
			String email = rs.getString(4);
			String address = rs.getString(5);
			String phone = rs.getString(6);
			String gender = rs.getString(7);
			Date DOB = rs.getDate(8);
			String sclass = rs.getString(9);
			String batch = rs.getString(10);
			String password = rs.getString(11);
			byte[] photo = rs.getBytes(12);
			int psize = rs.getInt(13);

			login_student = new Student(id, student_id, name, email, sclass, batch, address, phone, gender, DOB,
					password, photo, psize);

			// Proceed to dashboard
			new StudentSignInForm().toggleStudentSignInForm();
			AppMain.primaryStage.close();
			StuDashboardForm sForm = new StuDashboardForm();
			sForm.stuDashboardForm().show();

		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Login Failed");
			alert.setHeaderText("Incorrect Password");
			alert.setContentText("The password you entered is incorrect.");
			alert.show();
		}

		con.close();
	}

	@FXML
	void forgetPwdAction(ActionEvent event) throws IOException {
		new StudentSignInForm().toggleStudentSignInForm();
		ForgotPasswordForm f = new ForgotPasswordForm();
		f.forgotPasswordForm().show();
	}

	@FXML
	void registerLinkAction(ActionEvent event) throws IOException {
		new StudentSignInForm().toggleStudentSignInForm();
		new StudentRegisterationForm().toggleRegistrationForm();
	}
}