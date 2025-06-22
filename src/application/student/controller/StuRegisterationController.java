package application.student.controller;

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
import java.time.LocalTime;
import java.util.ResourceBundle;

import application.main.DatabaseConnection;
import application.main.form.IDGenerateForm;
import application.student.Student;
import application.student.form.StudentRegisterationForm;
import application.student.form.StudentSignInForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StuRegisterationController implements Initializable {
	private static final long IMAGE_SIZE_LIMIT = 300000;
	@FXML
	private TextField txtPhone;

	@FXML
	private TextField txtName;

	@FXML
	private ComboBox<String> cboPhone, cboClass, cboBatch;

	@FXML
	private TextArea txtAddress;

	@FXML
	private TextField txtEmail;

	@FXML
	private RadioButton optMale;

	@FXML
	private RadioButton optFemale;

	@FXML
	private Label Pwd;

	@FXML
	private PasswordField txtPw;

	@FXML
	private PasswordField txtConPwd;

	@FXML
	private Button btnRegister;

	@FXML
	private DatePicker dpDOB;

	private ToggleGroup genderToggleGroup;

	File selectedFile;

	File updateselectedFile;

	Student logStu = SignInController.login_student;
	@FXML
	private ImageView profilePhoto;

	private String selectedPhoto;

	public static boolean isFileChooserOpen = false;

	public static String getId;

	private Stage validationPopup;
	private Label first = new Label("\u2716"); // ❌
	private Label second = new Label("\u2716");
	private Label third = new Label("\u2716");
	private Label fourth = new Label("\u2716");
	private Label first1 = new Label("UPPER LETTER.!");
	private Label second1 = new Label("LOWER LETTER.!");
	private Label third1 = new Label("DIGITS.!");
	private Label fourth1 = new Label("SPECIAL CHARACTER.!");

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		method();
	}

	public void method() {
		genderToggleGroup = new ToggleGroup();
		optMale.setToggleGroup(genderToggleGroup);
		optFemale.setToggleGroup(genderToggleGroup);

		String[] phonePrefix = { "+95", "+44", "+1" };
		ObservableList<String> items = FXCollections.observableArrayList(phonePrefix);
		cboPhone.getItems().addAll(items);

		ObservableList<String> classPrefixes = FXCollections.observableArrayList("IP", "FE");
		cboClass.getItems().addAll(classPrefixes);

		ObservableList<String> batchPrefixes = FXCollections.observableArrayList("Batch1", "Batch2", "Batch3", "Batch4", "Batch5", "Batch6", "Batch7", "Batch8");
		cboBatch.getItems().addAll(batchPrefixes);

		txtPw.setOnMouseClicked(event -> createValidationPopup());
		txtPw.textProperty().addListener((observable, oldValue, newValue) -> {
			checkPasswordStrength(newValue);
		});

		// Add event listeners to close the popup when other fields are clicked
		addClosePopupListener(txtName);
		addClosePopupListener(txtEmail);
		addClosePopupListener(txtPhone);
		addClosePopupListener(txtAddress);
		addClosePopupListener(optMale);
		addClosePopupListener(optFemale);
		addClosePopupListener(dpDOB);
		addClosePopupListener(cboPhone);
		addClosePopupListener(cboClass);
		addClosePopupListener(cboBatch);
		addClosePopupListener(txtConPwd);

	}

	private void addClosePopupListener(TextField textField) {
		textField.setOnMouseClicked(event -> closeValidationPopup());
	}

	private void addClosePopupListener(TextArea textArea) {
		textArea.setOnMouseClicked(event -> closeValidationPopup());
	}

	private void addClosePopupListener(RadioButton gender) {
		gender.setOnMouseClicked(event -> closeValidationPopup());
	}

	private void addClosePopupListener(DatePicker datePicker) {
		datePicker.showingProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) { // If the popup closes
				closeValidationPopup();
			}
		});
	}

	private void addClosePopupListener(ComboBox<String> phone) {
		phone.setOnMouseClicked(event -> closeValidationPopup());
	}

	private void closeValidationPopup() {
		if (validationPopup != null && validationPopup.isShowing()) {
			validationPopup.close();
		}
	}

	@FXML
	void btnRegisterAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
		try {

			// Check for empty required fields
			if (txtName.getText().isEmpty() || txtEmail.getText().isEmpty() || txtPhone.getText().isEmpty()
					|| txtAddress.getText().isEmpty() || (!optMale.isSelected() && !optFemale.isSelected())
					|| dpDOB.getValue() == null || cboPhone.getValue() == null || cboClass.getValue() == null
					|| cboBatch.getValue() == null) {

				showAlert(AlertType.INFORMATION, "Required Data", "Fill all required data!",
						"Please fill in all required fields.");
				return;
			}

			// Convert email to lowercase
			String email = txtEmail.getText();
			txtEmail.setText(email);

			// For email validation (more robust)
			if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,}$")) {
			    showAlert(AlertType.WARNING, "Invalid Email", "Invalid Email Format",
			            "Please enter a valid Gmail address.");
			    return;
			}

			// For phone number validation (country code specific)
			String countryCode = cboPhone.getValue();
			if ("+95".equals(countryCode) && txtPhone.getLength() != 11) {
			    showAlert(AlertType.ERROR, "Invalid Phone Number", 
			            "Phone number must be 11 digits for Myanmar!",
			            "Please enter a valid phone number.");
			    return;
			}

			// Validate date of birth (Must be at least 14 years old)
			LocalDate dob = dpDOB.getValue();
			if (dob.isAfter(LocalDate.now().minusYears(14))) {
				showAlert(AlertType.ERROR, "Invalid Date of Birth", "Age must be at least 14 years!",
						"Please enter a valid date of birth.");
				return;
			}

			if (!isValidPhoneNumber(txtPhone.getText())) {
				showAlert(AlertType.ERROR, "Invalid Phone Number", "Phone number must be only Digit!",
						"Please check your phone number.");
				return;
			}

			// Validate password match
			if (!txtPw.getText().equals(txtConPwd.getText())) {
				showAlert(AlertType.WARNING, "Password Mismatch!", "Check Your Password",
						"Your passwords do not match!");
				return;
			}

			// Check for duplicate student email
			if (duplicateCheck()) {
				showAlert(AlertType.WARNING, "Duplicate Data", "Duplicate Account",
						"This email is already registered.");
				return;
			}
			if (txtPw.getLength() < 8) {
				showAlert(AlertType.WARNING, "Invalid Password", "Weak Password!",
						"Password must be at least 8 characters long.");
				return;
			}

			if (profilePhoto.getImage() == null || selectedFile == null) {
				showAlert(AlertType.WARNING, "Missing Photo", "No Profile Photo Selected",
						"Please upload a profile photo before registering.");
				return;
			}
			if (selectedFile.length() > IMAGE_SIZE_LIMIT) {
			    showAlert(AlertType.ERROR, "File Too Large", "Photo exceeds size limit", "Please choose a smaller image (max ~300KB).");
			    return;
			}

			// Save student data if all validations pass
			idFormat();
			saveStudentData();
			clearForm();
			method();
			closeValidationPopup();
			System.out.println();

		} catch (NullPointerException ex) {
			System.out.println("Unexpected error: " + ex.getMessage());
		}
	}

	public boolean duplicateCheck() throws ClassNotFoundException, SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("select count(*) from student where student_id=?");
		pst.setString(1, txtEmail.getText());

		try (ResultSet rs = pst.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		}
		return false;
	}

	public void idFormat() throws ClassNotFoundException, SQLException {
		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();
        int seconds = time.getSecond();
        int month = date.getMonthValue();
		String year = Integer.toString(date.getYear());

		String formattedMonth = String.format("%02d", month);
		String formattedSecond = String.format("%02d", seconds);

		int id = studentNumberGet();
		String formattedid = String.format("%02d", id + 1);

		getId = "MI-" + year + formattedMonth +formattedSecond+ formattedid;
	}

	public static int studentNumberGet() throws SQLException {
		int rowCount = 0;
		// Using try-with-resources for automatic resource management
		DatabaseConnection db = new DatabaseConnection();
		try (Connection con = db.getConnetion();
				PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) AS total_rows FROM student");
				ResultSet rs = pst.executeQuery()) {

			if (rs.next()) {
				rowCount = rs.getInt("total_rows");
			}
		}

		return rowCount + 1; // Increment row count for new student ID
	}

	private void clearForm() {
		txtName.clear();
		txtEmail.clear();
		txtPhone.clear();
		txtPw.clear();
		txtConPwd.clear();
		txtAddress.clear();
		cboPhone.setValue(null);
		cboClass.setValue(null);
		cboBatch.setValue(null);
		genderToggleGroup.selectToggle(null);
		dpDOB.setValue(null);
		profilePhoto.setImage(null);
		selectedFile = null;
	}

	private void checkPasswordStrength(String password) {

		boolean hasUpper = password.matches(".*[A-Z].*");
		updateValidationIcon(first, first1, hasUpper);

		boolean hasLower = password.matches(".*[a-z].*");
		updateValidationIcon(second, second1, hasLower);

		boolean hasDigit = password.matches(".*\\d.*");
		updateValidationIcon(third, third1, hasDigit);

		boolean hasSpecial = password.matches(".*[!@#$%^&*].*");
		updateValidationIcon(fourth, fourth1, hasSpecial);

	}

	private void updateValidationIcon(Label icon, Label label, boolean isValid) {
		if (isValid) {
			icon.setText("\u2714"); // ✔
			icon.setStyle("-fx-text-fill: green;");
			label.setStyle("-fx-text-fill: green;");
		} else {
			icon.setText("\u2716"); // ❌
			icon.setStyle("-fx-text-fill: red;");
			label.setStyle("-fx-text-fill: red;");
		}
	}

	private void createValidationPopup() {
		if (validationPopup == null) {
			validationPopup = new Stage();

			// Create a manual close button
			Button closeButton = new Button("\u2718");

			closeButton.setStyle("-fx-background-color: transparent; " + "-fx-text-fill: black; "
					+ "-fx-font-size: 16px; " + "-fx-border-width: 0; " + "-fx-cursor: hand;");

			// Disable hover effect
			closeButton.setOnMouseEntered(
					e -> closeButton.setStyle("-fx-background-color: transparent; " + "-fx-text-fill: red; "
							+ "-fx-font-size: 18px; " + "-fx-border-width: 0; " + "-fx-cursor: hand;"));

			closeButton.setOnMouseExited(
					e -> closeButton.setStyle("-fx-background-color: transparent; " + "-fx-text-fill: black; "
							+ "-fx-font-size: 16px; " + "-fx-border-width: 0; " + "-fx-cursor: hand;"));
			closeButton.setOnAction(e -> validationPopup.close());

			// Layout for close button
			StackPane closePane = new StackPane(closeButton);
			closePane.setAlignment(Pos.TOP_RIGHT);
			closePane.setPadding(new Insets(5));

			validationPopup.initStyle(StageStyle.UNDECORATED);
			validationPopup.setTitle("User ID Format");
			validationPopup.setAlwaysOnTop(true); // Keep the popup on top but do not block interaction
			validationPopup.setOpacity(1.0); // Fully visible popup
			validationPopup.setResizable(false);

			GridPane grid = new GridPane();
			grid.add(first, 0, 0);
			grid.add(first1, 1, 0);
			grid.add(second, 0, 1);
			grid.add(second1, 1, 1);
			grid.add(third, 0, 2);
			grid.add(third1, 1, 2);
			grid.add(fourth, 0, 3);
			grid.add(fourth1, 1, 3);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(10));

			AnchorPane mainLayout = new AnchorPane();
			mainLayout.getChildren().addAll(grid, closePane);
			AnchorPane.setTopAnchor(closePane, 5.0);
			AnchorPane.setRightAnchor(closePane, 5.0);
			Scene scene = new Scene(mainLayout, 350, 150);
			validationPopup.setScene(scene);
			validationPopup.setX(980);
			validationPopup.setY(300);
		}

		validationPopup.show();
	}

	public static boolean isValidPhoneNumber(String phoneNumber) {
		return phoneNumber.matches("\\d+");
	}

	public void saveStudentData() throws ClassNotFoundException, SQLException, IOException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		String gender = optMale.isSelected() ? "Male" : optFemale.isSelected() ? "Female" : "Not Specified";

		PreparedStatement pst = con.prepareStatement(
				"INSERT INTO student (student_id,name, email, address, phone, gender, date_of_birth, batch, class, password, photo, size) "
						+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		pst.setString(1, getId);
		pst.setString(2, txtName.getText());
		pst.setString(3, txtEmail.getText());
		pst.setString(4, txtAddress.getText());
		pst.setString(5, cboPhone.getValue() + " " + txtPhone.getText());
		pst.setString(6, gender);
		pst.setDate(7, Date.valueOf(dpDOB.getValue()));
		pst.setString(8, cboBatch.getValue());
		pst.setString(9, cboClass.getValue());
		pst.setString(10, txtPw.getText());

		if (selectedFile == null) {
			pst.setBinaryStream(11, null, 0);
			pst.setLong(12, 0);
		} else {
			Path path = Paths.get(selectedFile.getAbsolutePath());
			long fileSize = Files.size(path);
			if (fileSize > IMAGE_SIZE_LIMIT) {
				throw new IOException("Image size exceeds the 300 KB limit.");
			}
			pst.setBinaryStream(11, new FileInputStream(selectedFile), (int) fileSize);
			pst.setLong(12, fileSize);
		}

		pst.executeUpdate();

		// Instead of closing the form immediately, we show success first
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Registration Successful");
		alert.setHeaderText("Student registered successfully!");
		alert.showAndWait(); // Wait until user acknowledges the message

		IDGenerateForm form = new IDGenerateForm();
		form.iDGenerateForm().show();
		// Close the form after success
		StudentRegisterationForm.rForm.close();
	}

	@FXML
	void signInLinkAction(ActionEvent event) throws IOException {
//		System.out.println("Sign IN Form");

		StudentRegisterationForm.rForm.close();
		new StudentSignInForm().toggleStudentSignInForm();

	}

	@FXML
	private void btnBrowseAction() {
		isFileChooserOpen = true; // Prevent auto-close when FileChooser opens

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

		File file = fileChooser.showOpenDialog(null); // Open the file chooser
		if (file != null) {
			selectedFile = file; // Assign the selected file to the field
			selectedPhoto = file.getAbsolutePath();
			profilePhoto.setImage(new Image("file:" + selectedPhoto)); // Display selected image
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("No file was selected.");
			alert.show();
		}

		isFileChooserOpen = false; // Reset flag after FileChooser closes
	}

	private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}

}
