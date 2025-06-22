package application.admin.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import application.admin.form.UpdateTeacherInformation;
import application.main.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class UpdateTeacherInformationController implements Initializable {

	@FXML private TextArea txtTextField;
	@FXML private ComboBox<String> comboAction;
	@FXML private ImageView imgUpimage;
	@FXML private TextField txtfield;

	private File selectedFile;
	private int currentId = -1;
	private List<Integer> list_id;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		txtTextField.setWrapText(true);
		comboAction.setItems(FXCollections.observableArrayList("1", "0"));
		TeacherAbouts info = new TeacherAbouts();
		list_id = info.getAllIds();

		if (!list_id.isEmpty()) {
			currentId = list_id.get(0);
			extractData(currentId);
		} else {
			txtTextField.setText("No teacher information available.");
			System.out.println("No teachers with action = 1 or 0 found.");
		}
	}

	@FXML
	void btnUpBrowse(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG"),
				new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG"));
		selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			imgUpimage.setImage(new Image(selectedFile.toURI().toString()));
		}
	}

	@FXML
	void btnUpdateAction(ActionEvent event) {
		if (txtTextField.getText().isEmpty() || imgUpimage.getImage() == null) {
			showAlert(AlertType.WARNING, "Fill Data", "Please Fill Data!", "Please enter your data.");
			return;
		}
		try {
			dataAdd();
			showAlert(AlertType.INFORMATION, "Save Data", "Successfully Updated data!", "Thank you.");
			txtTextField.clear();
			imgUpimage.setImage(null);
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Update Failed", "Error updating data.", e.getMessage());
		}
	}

	private void dataAdd() throws SQLException, IOException {
		DatabaseConnection db = new DatabaseConnection();
		try (Connection con = db.getConnetion();
				PreparedStatement pst = con.prepareStatement(
						"UPDATE teacherinformation SET image = ?, description = ?, action = ? WHERE id = ?")) {
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imgUpimage.getImage(), null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", baos);
			byte[] imageBytes = baos.toByteArray();
			pst.setBinaryStream(1, new ByteArrayInputStream(imageBytes), imageBytes.length);
			pst.setString(2, txtTextField.getText());
			pst.setString(3, comboAction.getValue());
			pst.setInt(4, currentId);
			pst.executeUpdate();
		}
	}

	@FXML
	void btnCancle(ActionEvent event) {
		UpdateTeacherInformation.infoForm.close();
	}

	@FXML
	void btnNextAction(ActionEvent event) {
		int currentIndex = list_id.indexOf(currentId);
		if (currentIndex != -1 && currentIndex < list_id.size() - 1) {
			currentId = list_id.get(currentIndex + 1);
			extractData(currentId);
		} else {
			System.out.println("No next record available.");
		}
	}

	@FXML
	void btnBack(ActionEvent event) {
		int currentIndex = list_id.indexOf(currentId);
		if (currentIndex > 0) {
			currentId = list_id.get(currentIndex - 1);
			extractData(currentId);
		} else {
			System.out.println("No previous record available.");
		}
	}

	public void extractData(int id) {
		DatabaseConnection db = new DatabaseConnection();
		try (Connection con = db.getConnetion();
				PreparedStatement pst = con
						.prepareStatement("SELECT image, description, action FROM teacherinformation WHERE id = ?")) {
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				txtTextField.setText(rs.getString("description"));
				comboAction.setValue(rs.getString("action"));
				byte[] imageData = rs.getBytes("image");
				if (imageData != null) {
					imgUpimage.setImage(new Image(new ByteArrayInputStream(imageData)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void extractData(String teacherId) {
		DatabaseConnection db = new DatabaseConnection();
		try (Connection con = db.getConnetion();
				PreparedStatement pst = con.prepareStatement(
						"SELECT image, description, action, id FROM teacherinformation WHERE teach_id = ?")) {
			pst.setString(1, teacherId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				txtTextField.setText(rs.getString("description"));
				comboAction.setValue(rs.getString("action"));
				byte[] imageData = rs.getBytes("image");
				if (imageData != null) {
					imgUpimage.setImage(new Image(new ByteArrayInputStream(imageData)));
				}
				currentId = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void searchAction() {
		String searchText = txtfield.getText().trim().toLowerCase();
		if (searchText.isEmpty()) {
			showAlert(AlertType.WARNING, "Warning", "No search input provided", "Please enter a search term.");
			return;
		}

		String query = "SELECT teacher_id FROM teacher WHERE LOWER(teacher_id) LIKE ? OR LOWER(name) LIKE ? OR LOWER(email) LIKE ? OR "
				+ "LOWER(address) LIKE ? OR LOWER(phone) LIKE ? OR LOWER(gender) LIKE ? OR CAST(date_of_birth AS CHAR) LIKE ? OR "
				+ "LOWER(specialism) LIKE ?";

		DatabaseConnection db = new DatabaseConnection();
		try (Connection con = db.getConnetion(); PreparedStatement pstmt = con.prepareStatement(query)) {
			String wildcardSearch = "%" + searchText + "%";
			for (int i = 1; i <= 8; i++) {
				pstmt.setString(i, wildcardSearch);
			}
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				extractData(rs.getString("teacher_id"));
			} else {
				showAlert(AlertType.INFORMATION, "No Results", "No matching records found.", null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Database Error", "Search Failed", e.getMessage());
		}
		txtfield.clear();
	}

	private void showAlert(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
}

class TeacherAbouts {
	public List<Integer> getAllIds() {
		List<Integer> idList = new ArrayList<>();
		DatabaseConnection db = new DatabaseConnection();
		try (Connection con = db.getConnetion();
				PreparedStatement pst = con.prepareStatement("SELECT id FROM teacherinformation WHERE action IN (1, 0)");
				ResultSet rs = pst.executeQuery()) {
			while (rs.next()) {
				idList.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idList;
	}
}
