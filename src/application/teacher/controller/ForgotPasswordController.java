package application.teacher.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.teacher.form.ForgetForm;
import application.teacher.form.ForgotPasswordForm;
import application.main.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ForgotPasswordController {

	@FXML
	private TextField txtID;

	@FXML
	private TextField txtEmail;

	public static String localid;
	public static String password;

	@FXML
	void btnNext(ActionEvent event) throws IOException {
		DatabaseConnection db = new DatabaseConnection();
		try (Connection con = db.getConnetion()) {
			if (check()) {
				// Fetch password from the database
				String query1 = "SELECT password FROM teacher WHERE teacher_id = ?";
				PreparedStatement pst = con.prepareStatement(query1);
				pst.setString(1, txtID.getText());
				ResultSet rs = pst.executeQuery();
				if (rs.next())
					password = rs.getString("password");

				ForgetForm forgetForm = new ForgetForm();
				Stage stage = forgetForm.forgetForm(password);
				stage.show();
				ForgotPasswordForm.forgotForm.close();

			} else {
				showAlert("Incorrect Credentials!", "The ID or Email is incorrect. Please check your input.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert("Database Error", "Failed to connect to the database.");
		}
	}

	public boolean check() throws SQLException {
		boolean isValid = false;
		DatabaseConnection db = new DatabaseConnection();
		try (Connection con = db.getConnetion()) {
			String query1 = "SELECT email FROM teacher WHERE teacher_id = ?";
			String query2 = "SELECT teacher_id FROM teacher WHERE email = ?";

			// Check if the ID exists and get the email
			String getEmail = null;
			try (PreparedStatement pst = con.prepareStatement(query1)) {
				pst.setString(1, txtID.getText());
				ResultSet rs = pst.executeQuery();
				if (rs.next()) {
					getEmail = rs.getString("email");
				}
			}

			// Check if the email exists and get the admin_id
			String getID = null;
			try (PreparedStatement pst1 = con.prepareStatement(query2)) {
				pst1.setString(1, txtEmail.getText());
				ResultSet rs1 = pst1.executeQuery();
				if (rs1.next()) {
					getID = rs1.getString("teacher_id");
				}
			}

			// Validate both ID and Email
			if (txtID.getText().equals(getID) && txtEmail.getText().equals(getEmail)) {
				localid = txtID.getText();
				isValid = true;
			}
		}

		return isValid;
	}

	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.show();
	}
}
