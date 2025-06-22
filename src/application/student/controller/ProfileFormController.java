package application.student.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

import application.student.form.ColorPickerForm;
import application.main.DatabaseConnection;
import application.main.AppMain;
import application.student.form.ChangePasswordForm;
import application.student.form.LogOutForm;
import application.student.form.ProfileForm;
import application.student.form.StuDashboardForm;
import application.student.form.UpdateProfileForm;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;

public class ProfileFormController implements Initializable {

	@FXML
	private Label sNameLabel;
	@FXML
	private Label emailLabel;
	@FXML
	private Canvas canvas;

	@FXML
	private Pane profilePane;
	private File selected;
	private String id = SignInController.login_student.getStudent_id();
	private Preferences prefs = Preferences.userNodeForPackage(ProfileFormController.class);
	private boolean imageSelected = false;

	@FXML
	void linkProfile(ActionEvent event) throws SQLException, IOException {
		if (imageSelected) {
	        return;
	    }

	    imageSelected = true; // Prevent double trigger immediately

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");

		// Set extension filter
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
		fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

		// Show open file dialog
		// ✅ Set the current window as owner to make the dialog modal
        Window owner = profilePane.getScene().getWindow(); // Replace profilePane with your actual Pane reference
        selected = fileChooser.showOpenDialog(owner);
        if (selected != null) {
			// System.out.println(selectedFile.getAbsolutePath());
			Image image = new Image(selected.getAbsolutePath());
			drawCircularImage(image);

			DatabaseConnection db = new DatabaseConnection();
			Connection con = db.getConnetion();
			PreparedStatement pst = con.prepareStatement("update student set photo=?, size=? where student_id=?");
			FileInputStream fis = new FileInputStream(selected);
			pst.setBinaryStream(1, fis, (int) selected.length());
			Path path = Paths.get(selected.getAbsolutePath());
			long size = Files.size(path);
			pst.setFloat(2, size);
			pst.setString(3, id);
			if (size > 300000) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Image Size Error !");
				alert.setContentText("Your image size is too long. Please choice another image!");
				alert.show();

			} else {
				pst.executeUpdate();
			}

		}
	}

	private void drawCircularImage(Image image) {
		double width = canvas.getWidth();
		double height = canvas.getHeight();

		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, width, height);

		gc.save();
		gc.beginPath();
		gc.arc(width / 2, height / 2, width / 2, height / 2, 0, 360);
		gc.closePath();
		gc.clip();

		gc.drawImage(image, 0, 0, width, height);
		gc.restore();
	}

	@FXML
	void linkPassword(ActionEvent event) throws IOException {
		ChangePasswordForm pform = new ChangePasswordForm();
		pform.changePasswordForm().show();

	}

	@FXML
	void linkDeleteAccount(ActionEvent event) throws SQLException {

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Account");
		alert.setHeaderText("Your account will be deleted in 5 seconds!");
		alert.setContentText("Click 'Cancel' to stop this action.");

		ButtonType cancelButton = new ButtonType("Cancel");
		alert.getButtonTypes().setAll(cancelButton);

		// Countdown counter
		final int[] count = { 0 };
		final Timeline[] timeline = new Timeline[1];

		// Create the timeline for countdown
		timeline[0] = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			count[0]++;
			alert.setContentText(
					"Your account will be deleted in " + (5 - count[0]) + " seconds. Click 'Cancel' to stop.");

			if (count[0] == 5) {
				// Stop the timeline and delete the account
				timeline[0].stop();
				alert.close();
				deleteAccount();
			}
		}));

		timeline[0].setCycleCount(5); // Set the timeline to run for 5 seconds
		timeline[0].play(); // Start countdown automatically

		// Show the alert and wait for user's action
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == cancelButton) {
			timeline[0].stop(); // Stop countdown if user clicks Cancel
			alert.close();
		}
	}

// Method to delete account
	private void deleteAccount() {
		try {
			DatabaseConnection db = new DatabaseConnection();
			Connection con = db.getConnetion();
			PreparedStatement pst = con.prepareStatement("DELETE FROM student WHERE student_id=?");
			pst.setString(1, id);
			int rowsAffected = pst.executeUpdate();

			if (rowsAffected > 0) {
				closeAllForms();
				AppMain.primaryStage.show();
			} else {
				System.out.println("No record found with the given ID.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void btnCancle(ActionEvent event) {

		if (ProfileForm.profileStage != null && ProfileForm.profileStage.isShowing()) {
			ProfileForm.profileStage.close();
			ProfileForm.profileStage = null;
		}
	}

	@FXML
	void linkPColorChange(ActionEvent event) {
		try {
			// Open the Color Picker and pass the callback
			ColorPickerForm.showColorPicker(new Consumer<Color>() {
				@Override
				public void accept(Color selectedColor) {
					String colorHex = toHex(selectedColor);
					profilePane.setStyle("-fx-background-color: " + colorHex + ";");

					// Save the color
					prefs.put("profileColor", colorHex);
				}
			}, new Consumer<String>() { // Added avatar callback
				@Override
				public void accept(String avatarPath) {
					setAvatarImage(avatarPath); // Update avatar image in ProfileForm

				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String toHex(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
	}

	public void setAvatarImage(String avatarPath) {
		if (avatarPath != null) {
			Image image = new Image("file:" + avatarPath);
			drawCircularImage(image);
		} else {
			System.out.println("Error: avatarPath is null");
		}
	}

	public void getPhotoNameAndEmail() throws SQLException {

		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		PreparedStatement pst = con.prepareStatement("select name,email,photo from student where student_id=?");
		pst.setString(1, id);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			String name = rs.getString("name");
			String email = rs.getString("email");
			sNameLabel.setText(name);
			emailLabel.setText(email);
			Image image;
			byte[] imageData = rs.getBytes("photo");
			if (imageData != null) {
				image = new Image(new ByteArrayInputStream(imageData));
			} else {
				image = new Image(
						"file:D:/Eclipse/JavaWorkspace/ITPECOldQuestionProject2/src/application/images/images.png");
			}
			drawCircularImage(image);
		}
	}

	private void closeAllForms() {
		if (LogOutForm.logOutStage != null && LogOutForm.logOutStage.isShowing()) {
			LogOutForm.logOutStage.close();
			LogOutForm.logOutStage = null;
		}

		if (ProfileForm.profileStage != null && ProfileForm.profileStage.isShowing()) {
			ProfileForm.profileStage.close();
			ProfileForm.profileStage = null;
		}

		if (StuDashboardForm.sdfForm != null && StuDashboardForm.sdfForm.isShowing()) {
			StuDashboardForm.sdfForm.close();
			StuDashboardForm.sdfForm = null;
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {

			String savedColor = prefs.get("profileColor", "#777B7C"); // Default color if not found
			profilePane.setStyle("-fx-background-color: " + savedColor + ";");
			getPhotoNameAndEmail();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void updateAccount(ActionEvent event) throws IOException {
		new UpdateProfileForm().toggleUpdateProfileForm();;
	}

}
