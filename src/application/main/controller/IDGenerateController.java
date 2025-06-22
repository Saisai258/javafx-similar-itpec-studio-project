package application.main.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.main.DatabaseConnection;
import application.main.form.IDGenerateForm;
import application.student.controller.StuRegisterationController;
import application.teacher.controller.TeacherRegisterationFormController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class IDGenerateController implements Initializable {

	@FXML
	private TextField getId;
	@FXML
	private TextField password;
	

	@FXML
	void btnCancle(ActionEvent event) throws IOException {
		Clipboard clipboard = Clipboard.getSystemClipboard();
	    ClipboardContent content = new ClipboardContent();
	    content.putString(getId.getText());
	    clipboard.setContent(content);
		IDGenerateForm.idForm.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (StuRegisterationController.getId != null) {
			getId.setText(StuRegisterationController.getId);
			password.setVisible(false);
		} else if (TeacherRegisterationFormController.getId != null) {
			getId.setText(TeacherRegisterationFormController.getId);
			try {
				passGet();
				password.setVisible(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			getId.setText("No ID available");
			password.setVisible(false);
		}
	}

	public void passGet() throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("SELECT password FROM teacher WHERE teacher_id=?");
		pst.setString(1, TeacherRegisterationFormController.getId);

		System.out.println("Executing query for ID: " + TeacherRegisterationFormController.getId);
		try (ResultSet rs = pst.executeQuery()) {
			if (rs.next()) {
				String get = rs.getString("password");
				// Set password in the TextField
				password.setText(get);

				// Ensure visibility and usability
				password.setEditable(true);
				password.setDisable(false);
				password.setVisible(true);
			} else {
				System.out.println("No password found for this ID.");
			}
		}
	}
}
