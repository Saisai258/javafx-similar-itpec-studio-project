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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import application.admin.form.ChangePasswordForm;
import application.admin.form.ProfileForm;
import application.admin.form.UpdateProfileForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import application.main.DatabaseConnection;

public class UpdateProfileController implements Initializable {

	@FXML
	private Button btncancel;

	@FXML
	private TextField txtid;

	@FXML
	private TextField txtphone;

	@FXML
	private TextField txtemail;

	@FXML
	private TextField txtname;

	@FXML
	private DatePicker datepick;

	@FXML
	private RadioButton optmale;

	@FXML
	private ComboBox<String> combobox;

	@FXML
	private RadioButton optfemale;

	@FXML
	private TextArea txtaddress;

	@FXML
	private Button btnupdate;

	@FXML
	private ToggleGroup gendertoggleGroup;

	@FXML
	private ImageView imgProfile;

	File selectedFile;
	private String id = SignInController.id;

	public String[] patternCheck(String data, String splitSymbol) {
		String[] phone = new String[2];
		phone = data.split(splitSymbol);
		return phone;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			getInfo();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void changepasswordLinkAction(ActionEvent event) throws IOException {
		System.out.println("Register Form!");

		ChangePasswordForm cpf = new ChangePasswordForm();
		cpf.changePasswordForm();
		ChangePasswordForm.changePasswordForm.show();

	}

	@FXML
	void btncancellinkAction(ActionEvent event) {
		UpdateProfileForm.updForm.close();
	}

	@FXML
	void btnUpdateAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
	    if (!validation()) {
	        showAlert(Alert.AlertType.WARNING, "Missing Fields", "All fields are required!", "Please complete all fields.");
	        return;  // Stop if validation fails
	    }
	    confirm();
	    getInfo();
	    
	}

	private boolean validation() {

	    // Reset styles
	    txtname.setStyle(null);
	    txtemail.setStyle(null);
	    txtphone.setStyle(null);
	    datepick.setStyle(null);
	    combobox.setStyle(null);
	    optmale.setStyle(null);
	    optfemale.setStyle(null);
	    txtaddress.setStyle(null);
	    imgProfile.setStyle(null);

	    // Name
	    if (txtname.getText().isEmpty()) {
	        txtname.setStyle("-fx-border-color: red;");
	        return false;
	    } else {
	        txtname.setStyle("-fx-border-color: green;");
	    }

	    // Email
	    if (txtemail.getText().isEmpty() || !isValidGmail(txtemail.getText())) {
	        txtemail.setStyle("-fx-border-color: red;");
	        return false;
	    } else {
	        txtemail.setStyle("-fx-border-color: green;");
	    }
	 // Date of Birth
	    LocalDate date = datepick.getValue();
	    if (date == null || date.isAfter(LocalDate.now().minusYears(18))) {
	        datepick.setStyle("-fx-border-color: red;");
	        return false;
	    } else {
	        datepick.setStyle("-fx-border-color: green;");
	    }

	    // Phone code comboBox
	    if (combobox.getValue() == null) {
	        combobox.setStyle("-fx-border-color: red; -fx-text-fill: red;");
	        return false;
	    } else {
	        combobox.setStyle("-fx-border-color: green;");
	    }

	    // Phone
	    if (txtphone.getText().isEmpty() || !isValidPhoneNumber(txtphone.getText()) || txtphone.getLength() != 11) {
	        txtphone.setStyle("-fx-border-color: red;");
	        return false;
	    } else {
	        txtphone.setStyle("-fx-border-color: green;");
	    }

	    // Gender
	    if (!optmale.isSelected() && !optfemale.isSelected()) {
	        optmale.setStyle("-fx-border-color: red;");
	        optfemale.setStyle("-fx-border-color: red;");
	        return false;
	    } else {
	        optmale.setStyle("-fx-border-color: green;");
	        optfemale.setStyle("-fx-border-color: green;");
	    }

	    // Address (only letters and spaces)
	    if (txtaddress.getText().matches("[a-zA-Z ]+")) {
	        txtaddress.setStyle("-fx-border-color: green;");
	    } else {
	        txtaddress.setStyle("-fx-border-color: red;");
	        showAlert(Alert.AlertType.WARNING, "Invalid Address", "Address should contain only letters and spaces.", "Please correct the address.");
	        return false;
	    }

	    return true;
	}


//	 image browse
	@FXML
	void onBrowsePhoto(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");

		// Set extension filter
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
		fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

		// Show open file dialog
		selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			// System.out.println(selectedFile.getAbsolutePath());
			Image image = new Image(selectedFile.getAbsolutePath());
			imgProfile.setImage(image);

		}
	}

//	get image from database
	public void imageGet() throws SQLException {

		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		PreparedStatement pst1 = con.prepareStatement("select photo from admin where admin_id=?");
		pst1.setString(1, SignInController.id);
		ResultSet rs1 = pst1.executeQuery();
		if (rs1.next()) {
			byte[] imageData = rs1.getBytes(1);
			if (imageData != null) {
				Image image = new Image(new ByteArrayInputStream(imageData));
				imgProfile.setImage(image);
			} else {
				Image defaultImage = new Image(
						"D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images\\images.png");
				imgProfile.setImage(defaultImage);
			}
		}
	}

	public static boolean isValidGmail(String email) {
		return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,}$");
	}

	public static boolean isValidPhoneNumber(String phoneNumber) {
		return phoneNumber.matches("09\\d{9}");
	}

	private void confirm() throws SQLException, IOException {
		if (selectedFile == null || selectedFile.getAbsolutePath().isEmpty()) {
			secondSave();
		} else {
			save();
		}
		UpdateProfileForm.updForm.close();
		ProfileForm.closeForm();
	}

	private void save() throws SQLException, IOException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement(
				"update admin set name=?, email=?  ,address=?, phone=? ,gender=? ,date_of_birth=?  ,photo=? , size=? where admin_id=? ");

		pst.setString(1, txtname.getText());

		pst.setString(2, txtemail.getText());

		pst.setString(3, txtaddress.getText());
		pst.setString(4, combobox.getValue() + " " + txtphone.getText());

		String gender = null;
		if (optmale.isSelected()) {
			gender = optmale.getText();
		}
		if (optfemale.isSelected()) {
			gender = optfemale.getText();
		}
		pst.setString(5, gender);
		LocalDate selectedDate = datepick.getValue();
		Date sqlDate = Date.valueOf(selectedDate);
		pst.setDate(6, sqlDate);

		if (selectedFile != null) {
			FileInputStream fis = new FileInputStream(selectedFile);
			pst.setBinaryStream(7, fis, (int) selectedFile.length());

			Path path = Paths.get(selectedFile.getAbsolutePath());
			long size = Files.size(path);

			if (size < 300000) {
				pst.setFloat(8, size);
			} else {
				System.out.println("Your Image File Size is Too Large.");
				pst.setNull(7, java.sql.Types.BLOB); // Set NULL if size exceeds limit
				pst.setNull(8, java.sql.Types.FLOAT);
			}
		} else {
			System.out.println("No file selected. Setting NULL for photo and size.");
			pst.setNull(7, java.sql.Types.BLOB); // Set NULL for photo
			pst.setNull(8, java.sql.Types.FLOAT); // Set NULL for size
		}

		pst.setString(9, txtid.getText());
		pst.executeUpdate();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Update Data");
		alert.setContentText("Successful Update data!");
		alert.show();

	}

	private void secondSave() throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement(
				"update admin set name=?, email=?  ,address=?, phone=? ,gender=? ,date_of_birth=? where admin_id=? ");

		pst.setString(1, txtname.getText());

		pst.setString(2, txtemail.getText());

		pst.setString(3, txtaddress.getText());
		pst.setString(4, combobox.getValue() + " " + txtphone.getText());
		String gender = null;
		if (optmale.isSelected()) {
			gender = optmale.getText();
		}
		if (optfemale.isSelected()) {
			gender = optfemale.getText();
		}
		pst.setString(5, gender);
		LocalDate selectedDate = datepick.getValue();
		Date sqlDate = Date.valueOf(selectedDate);
		pst.setDate(6, sqlDate);
		pst.setString(7, txtid.getText());
		pst.executeUpdate();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Update Data");
		alert.setContentText("Successful Update data!");
		alert.show();

	}

	private void getInfo() throws SQLException {
		String[] phonePrefix = { "+95", "+44", "+1", "+91" };
		ObservableList<String> items = FXCollections.observableArrayList(phonePrefix);
		combobox.getItems().addAll(items);
		try {
			imageGet();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		PreparedStatement pst = con.prepareStatement("select * from admin where admin_id=?");
		pst.setString(1, id);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			String id = rs.getString("admin_id");
			String name = rs.getString("name");
			String email = rs.getString("email");
			String address = rs.getString("address");
			String phone = rs.getString("phone");
			String gender = rs.getString("gender");
			Date sqlDate = rs.getDate("date_of_birth");
			txtid.setText(id);
			txtname.setText(name);
			txtemail.setText(email);
			txtaddress.setText(address);
			if (gender.equals("Male"))
				optmale.setSelected(true);
			else
				optfemale.setSelected(true);
			gendertoggleGroup = new ToggleGroup();
			optmale.setToggleGroup(gendertoggleGroup);
			optfemale.setToggleGroup(gendertoggleGroup);

			if (phone != null && phone.contains(" ")) {
				String[] prefixArray = phone.split(" ", 2); // Split into at most 2 parts
				System.out.println("Country Code: " + prefixArray[0]);
				System.out.println("Phone Number: " + prefixArray[1]);

				// Set values only if the split is successful
				if (prefixArray.length == 2) {
					combobox.setValue(prefixArray[0]); // Country code
					txtphone.setText(prefixArray[1]); // Phone number
				}
			} else {
				System.out.println("Phone number does not contain a space. Setting default values.");
				combobox.setValue("+95"); // Default country code
				txtphone.setText(phone != null ? phone : ""); // Avoid null pointer exception
			}

			LocalDate formDate = sqlDate.toLocalDate();
			datepick.setValue(formDate);
		}
	}

	private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}

}
