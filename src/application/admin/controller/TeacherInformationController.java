package application.admin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import application.admin.form.TeacherInformationForm;
import application.main.DatabaseConnection;
import application.main.form.IDGenerateForm;
import application.teacher.Teacher;
import application.teacher.controller.TeacherRegisterationFormController;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class TeacherInformationController {

	public static Teacher teacher_info;
	@FXML
	private TextArea txtDescription;

	@FXML
	private ImageView teacherImage;

	File questionImage;
	public int actionNum = 1;
	@FXML
	void btnBrowse(ActionEvent event) {
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Open Image File");

	    // Set extension filter
	    FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
	    FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
	    fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

	    // Show open file dialog
	    questionImage = fileChooser.showOpenDialog(null);
	    if (questionImage != null) {
	        Image image = new Image(questionImage.toURI().toString());
	        
	        teacherImage.setImage(image); // Set the image first
	       
	    }
	}

	@FXML
	void btnInformationAdd(ActionEvent event) throws SQLException, IOException {

		try {
			if (txtDescription.getText().equals("") || teacherImage == null) {

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Fill Data");
				alert.setHeaderText("Please Fill Data!");
				alert.setContentText("Please enter your data.");
				alert.show();
			} else {
				
				dataAdd();				

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Save Data");
				alert.setHeaderText("Successfully Saved data!");
				alert.setContentText("Please you need to Login.");
				alert.show();
				
				txtDescription.setText("");
				teacherImage.setImage(null);
				
				PauseTransition delay = new PauseTransition(Duration.seconds(3));
				delay.setOnFinished(e -> alert.close());
				delay.play();
				TeacherInformationForm tinfo = new TeacherInformationForm();

				// To open the form
//				tinfo.showForm();

				// To close the form
				tinfo.closeForm();

				
				IDGenerateForm form = new IDGenerateForm();
				form.iDGenerateForm().show();
			}
		} catch (NullPointerException ex) {
			System.out.println("Not Null");
		}

	}

	public void dataAdd() throws SQLException, FileNotFoundException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("insert into teacherinformation (image,description,action,teach_id) values (?,?,?,?)");
		FileInputStream image = new FileInputStream(questionImage);
		pst.setBinaryStream(1, image, (int) questionImage.length());
		pst.setString(2, txtDescription.getText());
		pst.setInt(3, actionNum);
		pst.setString(4, TeacherRegisterationFormController.getId);
		pst.executeUpdate();
	}

}
