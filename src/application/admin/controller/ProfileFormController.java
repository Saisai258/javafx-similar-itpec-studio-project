package application.admin.controller;

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

import application.admin.form.UpdateProfileForm;
import application.admin.form.ChangePasswordForm;
import application.admin.form.ColorPickerForm;
import application.admin.form.HomeForm1;
import application.admin.form.LogOutForm;
import application.admin.form.ProfileForm;
import application.admin.form.RegisterChoiceForm;
import application.main.DatabaseConnection;
import application.main.AppMain;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
	private String id = SignInController.id;
	private Preferences prefs = Preferences.userNodeForPackage(ProfileFormController.class);
	private boolean imageSelected = false;
	
	@FXML
	void linkProfile(ActionEvent event) throws SQLException, IOException {
	    if (imageSelected) {
	        return;
	    }

	    imageSelected = true; // Prevent double trigger immediately

	    Platform.runLater(() -> {
	        try {
	            UpdateProfileForm.closeForm();

	            FileChooser fileChooser = new FileChooser();
	            fileChooser.setTitle("Open Image File");

	            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
	            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
	            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

	         // ✅ Set the current window as owner to make the dialog modal
	            Window owner = profilePane.getScene().getWindow(); // Replace profilePane with your actual Pane reference
	            selected = fileChooser.showOpenDialog(owner);

	            if (selected != null) {
	                Image image = new Image(selected.toURI().toString());
	                drawCircularImage(image);

	                DatabaseConnection db = new DatabaseConnection();
	                Connection con = db.getConnetion();
	                PreparedStatement pst = con.prepareStatement("UPDATE admin SET photo = ?, size = ? WHERE admin_id = ?");
	                FileInputStream fis = new FileInputStream(selected);
	                pst.setBinaryStream(1, fis, (int) selected.length());
	                Path path = Paths.get(selected.getAbsolutePath());
	                long size = Files.size(path);
	                pst.setFloat(2, size);
	                pst.setString(3, id);

	                if (size > 300000) {
	                    Alert alert = new Alert(AlertType.WARNING);
	                    alert.setTitle("Image Size Error !");
	                    alert.setContentText("Your image size is too large. Please choose another image!");
	                    alert.show();
	                } else {
	                    pst.executeUpdate();
	                }

	                pst.close();
	                con.close();
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    });
	}

	public void drawCircularImage(Image image) {
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
		UpdateProfileForm.closeForm();
		ChangePasswordForm pform = new ChangePasswordForm();
		pform.changePasswordForm().show();

	}

	@FXML
	void linkDeleteAccount(ActionEvent event) throws SQLException {
		UpdateProfileForm.closeForm();
		// Check if there is more than one admin account
		if (getAdminCount() <= 1) {
			Alert warningAlert = new Alert(Alert.AlertType.WARNING);
			warningAlert.setTitle("Warning");
			warningAlert.setHeaderText("Cannot Delete Account!");
			warningAlert.setContentText("At least one admin account must remain in the system.");
			warningAlert.showAndWait();
			return; // Stop deletion process
		}

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

	private int getAdminCount() throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		String query = "SELECT COUNT(*) FROM admin";
		PreparedStatement pst = con.prepareStatement(query);
		ResultSet rs = pst.executeQuery();

		int count = 0;
		if (rs.next()) {
			count = rs.getInt(1);
		}
		rs.close();
		pst.close();
		con.close();
		return count;
	}

// Method to delete account
	private void deleteAccount() {
		try {
			DatabaseConnection db = new DatabaseConnection();
			Connection con = db.getConnetion();
			PreparedStatement pst = con.prepareStatement("DELETE FROM admin WHERE admin_id=?");
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
		UpdateProfileForm.closeForm();
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

	@FXML
	void btnUpdate(ActionEvent event) throws IOException {
		UpdateProfileForm form = new UpdateProfileForm();
		form.updateProfile().show();
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

		PreparedStatement pst = con.prepareStatement("select name,email,photo from admin where admin_id=?");
		pst.setString(1, id);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			String name = rs.getString("name");
			String email = rs.getString("email");
			sNameLabel.setText(name);
			emailLabel.setText(email);
			byte[] imageData = rs.getBytes("photo");
			if (imageData != null) {
				Image image = new Image(new ByteArrayInputStream(imageData));
				drawCircularImage(image);
			} else {
				Image defaultImage = new Image(
						"D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images\\images.png"); // Or
																														// wherever
																														// your
																														// default
																														// image
																														// is
																														// stored
				drawCircularImage(defaultImage);
			}
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

		if (RegisterChoiceForm.rForm != null && RegisterChoiceForm.rForm.isShowing()) {
			RegisterChoiceForm.rForm.close();
			RegisterChoiceForm.rForm = null;
		}

		if (HomeForm1.homeStage != null && HomeForm1.homeStage.isShowing()) {
			HomeForm1.homeStage.close();
			HomeForm1.homeStage = null;
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

}
