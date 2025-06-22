package application.admin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import application.admin.form.RegisterationForm;
import application.main.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RegisterationController implements Initializable {
	private static final long IMAGE_SIZE_LIMIT = 300000; // 300 KB limit

	@FXML
	private TextField txtId, txtName, txtEmail, txtPhone, tetTfield;

	@FXML
	private ComboBox<String> cboPhone;

	@FXML
	private RadioButton optMale, optFemale;

	@FXML
	private DatePicker dpDOB;

	@FXML
	private PasswordField txtPw, conpas;

	@FXML
	private ImageView photoImgView;

	private ToggleGroup genderToggleGroup;
	private File selectedFile;

	private Stage validationPopup;
	private Label iconCharStart = new Label("\u2716"); // ❌
	private Label iconAlphaPart = new Label("\u2716");
	private Label iconDigitPart = new Label("\u2716");
	private Label lblCharStart = new Label("First character must be 'A'");
	private Label lblAlphaPart = new Label("Middle part must be letters (one or more)");
	private Label lblDigitPart = new Label("Last part must be exactly 7 digits");

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
		method();
		
		Tooltip.install(txtId, new Tooltip("Format: A + letters + 7 digits"));
		Tooltip.install(txtPw, new Tooltip( "Format:\n" +
			    "✔ At least one uppercase letter (A–Z)\n" +
			    "✔ At least one lowercase letter (a–z)\n" +
			    "✔ At least one digit (0–9)\n" +
			    "✔ At least one special character (!@#$%^&*)"));
		Tooltip.install(txtEmail, new Tooltip("Format: example@domain.com"));
		Tooltip.install(txtPhone, new Tooltip("Enter a valid Myanmar phone number (e.g., 0912345678)"));
		
	}
	@FXML
	void btnRegisterAction(ActionEvent event) {
		try {
			if (validateFields()) {
				if (!duplicateCheck()) {
					saveAdminData();
					showAlert(Alert.AlertType.INFORMATION, "Success", "Registration Successful!",
							"You have successfully registered.");
					clearFields();
					method();
				} else {
					showAlert(Alert.AlertType.WARNING, "Duplicate Record", "Account exists!",
							"This Admin ID already exists.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Error", "An error occurred!", e.getMessage());
		}
	}

	private void method() {
		genderToggleGroup = new ToggleGroup();
		optMale.setToggleGroup(genderToggleGroup);
		optFemale.setToggleGroup(genderToggleGroup);

		String[] phonePrefix = { "+95"};
		cboPhone.setItems(FXCollections.observableArrayList(phonePrefix));
		txtId.setOnMouseClicked(event -> createValidationPopup(txtId));
		txtPw.setOnMouseClicked(event -> createValidationPopup(txtPw));

		txtId.textProperty().addListener((observable, oldValue, newValue) -> {
		    updateValidationPopup(newValue);
		    if (isValidUserID(newValue)) {
		        txtId.setStyle("-fx-border-color: green;");
		    } else {
		        txtId.setStyle("-fx-border-color: red;");
		    }
		});
		cboPhone.valueProperty().addListener((obs, oldVal, newVal) -> {
		    validatePhonePrefix(cboPhone);
		});

		txtPw.textProperty().addListener((observable, oldValue, newValue) -> {
			checkPasswordStrength(newValue);
			
		});

		addClosePopupListener(txtName);
		addClosePopupListener(txtEmail);
		addClosePopupListener(txtPhone);
		addClosePopupListener(tetTfield);
		addClosePopupListener(optMale);
		addClosePopupListener(optFemale);
		addClosePopupListener(dpDOB);
		addClosePopupListener(cboPhone);
		addClosePopupListener(conpas);
		
		txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (isValidPhoneNumber(newValue)) {
		        txtPhone.setStyle("-fx-border-color: green;"); 
		    } else {
		        txtPhone.setStyle("-fx-border-color: red;");   
		    }	    
		});
		
		txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
		    updateValidationPopup(newValue);
		    if (isValidGmail(newValue)) {
		    	txtEmail.setStyle("-fx-border-color: green;");
		    } else {
		    	txtEmail.setStyle("-fx-border-color: red;");
		    }
		});
		

	}  //method end
	
	private boolean validateFields() {
	    boolean isValid = true;

	    // Reset all styles
	    txtId.setStyle(null);
	    txtName.setStyle(null);
	    txtEmail.setStyle(null);
	    txtPhone.setStyle(null);
	    txtPw.setStyle(null);
	    conpas.setStyle(null);
	    dpDOB.setStyle(null);
	    cboPhone.setStyle(null);
	    tetTfield.setStyle(null);
	    optMale.setStyle(null);
	    optFemale.setStyle(null);
	    photoImgView.setStyle(null);


	    // --- ID ---
	    if (txtId.getText().isEmpty() || !isValidUserID(txtId.getText())) {
	        txtId.setStyle("-fx-border-color: red;");
	        isValid = false;
	    } else {
	        txtId.setStyle("-fx-border-color: green;");
	    }

	    // --- Name ---
	    if (txtName.getText().isEmpty()) {
	        txtName.setStyle("-fx-border-color: red;");
	        isValid = false;
	    } else {
	        txtName.setStyle("-fx-border-color: green;");
	    }

	    // --- Email ---
	    if (txtEmail.getText().isEmpty() || !isValidGmail(txtEmail.getText())) {
	        txtEmail.setStyle("-fx-border-color: red;");
	        isValid = false;
	    } else {
	        txtEmail.setStyle("-fx-border-color: green;");
	    }

	    // --- Phone ---
	    if (txtPhone.getText().isEmpty() || !isValidPhoneNumber(txtPhone.getText()) || txtPhone.getLength() != 11) {
	        txtPhone.setStyle("-fx-border-color: red;");
	        isValid = false;
	    } else {
	        txtPhone.setStyle("-fx-border-color: green;");
	    }

	    // --- Gender ---
	    if (!optMale.isSelected() && !optFemale.isSelected()) {
	        optMale.setStyle("-fx-border-color: red;");
	        optFemale.setStyle("-fx-border-color: red;");
	        isValid = false;
	    } else {
	        optMale.setStyle("-fx-border-color: green;");
	        optFemale.setStyle("-fx-border-color: green;");
	    }

	    // --- DOB ---
	    LocalDate date = dpDOB.getValue();
	    if (date == null || date.isAfter(LocalDate.now().minusYears(18))) {
	        dpDOB.setStyle("-fx-border-color: red;");
	        isValid = false;
	    } else {
	        dpDOB.setStyle("-fx-border-color: green;");
	    }

	    // --- Phone Code ComboBox ---
	    if (cboPhone.getValue() == null) {
	        cboPhone.setStyle("-fx-border-color: red; -fx-text-fill: red;");
	        isValid = false;
	    } else {
	        cboPhone.setStyle("-fx-border-color: green;");
	    }

	    // --- Password ---
	    if (txtPw.getText().isEmpty() || txtPw.getLength() < 8) {
	        txtPw.setStyle("-fx-border-color: red;");
	        isValid = false;
	    } else {
	        txtPw.setStyle("-fx-border-color: green;");
	    }

	    // --- Confirm Password ---
	    if (conpas.getText().isEmpty() || !conpas.getText().equals(txtPw.getText())) {
	        conpas.setStyle("-fx-border-color: red;");
	        isValid = false;
	    } else {
	        conpas.setStyle("-fx-border-color: green;");
	    }

	    // --- Specialism ---
	    if (tetTfield.getText().isEmpty()) {
	        tetTfield.setStyle("-fx-border-color: red;");
	        isValid = false;
	    } else {
	        tetTfield.setStyle("-fx-border-color: green;");
	    }

	    // --- Photo ---
	    if (selectedFile == null) {
	        photoImgView.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
	        isValid = false;
	    } else {
	        photoImgView.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
	    }

	    // --- Final alert if any field is still invalid ---
	    if (!isValid) {
	        showAlert(Alert.AlertType.WARNING, "Missing Fields", "All fields are required!", "Please complete all fields.");
	        return false;
	    }

	    // Password match
	    if (!txtPw.getText().equals(conpas.getText())) {
	        showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match!", "Please recheck passwords.");
	        return false;
	    }

	    // Validate formats
	    if (!isValidUserID(txtId.getText())) {
	        showAlert(Alert.AlertType.ERROR, "Invalid ID", "User ID is incorrect!", "Check the validation box.");
	        return false;
	    }

	    if (!isValidGmail(txtEmail.getText())) {
	        showAlert(Alert.AlertType.ERROR, "Invalid Email", "Incorrect Gmail Format!", "Use format: user@gmail.com.");
	        return false;
	    }

	    if (!isValidPhoneNumber(txtPhone.getText())) {
	        showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Digits only!", "Please check your phone number.");
	        return false;
	    }

	    if (txtPhone.getLength() != 11) {
	        showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Length must be exactly 11!", "Please enter a valid number.");
	        return false;
	    }

	    // Date of birth validation
	    LocalDate date1 = dpDOB.getValue();
	    if (date1.isAfter(LocalDate.now().minusYears(18))) {
	        showAlert(Alert.AlertType.ERROR, "Invalid Date of Birth", "You must be at least 18 years old.", "Enter a valid date.");
	        return false;
	    }

	    // Password length
	    if (txtPw.getLength() < 8) {
	        showAlert(AlertType.WARNING, "Weak Password", "Password too short!", "Must be at least 8 characters.");
	        return false;
	    }

	    return true;
	}


	private void addClosePopupListener(TextField textField) {
		textField.setOnMouseClicked(event -> closeValidationPopup());
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

	private void updateValidationPopup(String userId) {  //user ID check
	 
	    boolean validStart = !userId.isEmpty() && userId.charAt(0) == 'A';
	    updateValidationIcon(iconCharStart, lblCharStart, validStart);

	    boolean validAlphaPart = false;
	    if (userId.length() > 1) {
	        String lettersPart = userId.substring(1).replaceAll("\\d", "");
	        validAlphaPart = lettersPart.matches("[A-Za-z]*");
	    }
	    updateValidationIcon(iconAlphaPart, lblAlphaPart, validAlphaPart);

	    boolean validDigitPart = userId.matches("A[A-Za-z]*\\d{7}");
	    updateValidationIcon(iconDigitPart, lblDigitPart, validDigitPart);
	}


	private void checkPasswordStrength(String password) { // password check

		boolean hasUpper = password.matches(".*[A-Z].*");
		updateValidationIcon(first, first1, hasUpper);

		boolean hasLower = password.matches(".*[a-z].*");
		updateValidationIcon(second, second1, hasLower);

		boolean hasDigit = password.matches(".*\\d.*");
		updateValidationIcon(third, third1, hasDigit);

		boolean hasSpecial = password.matches(".*[!@#$%^&*].*");
		updateValidationIcon(fourth, fourth1, hasSpecial);
		
		if (hasUpper && hasLower && hasDigit && hasSpecial && password.length() >= 8) {
			txtPw.setStyle("-fx-border-color: green;");
	    } else {
	    	txtPw.setStyle("-fx-border-color: red;");
	    }

	}

	private void createValidationPopup(TextField sourceField) {   // checking form
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
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(10));
			if (sourceField == txtId) {
				grid.add(iconCharStart, 0, 0);
				grid.add(lblCharStart, 1, 0);
				grid.add(iconAlphaPart, 0, 1);
				grid.add(lblAlphaPart, 1, 1);
				grid.add(iconDigitPart, 0, 2);
				grid.add(lblDigitPart, 1, 2);
			} else if (sourceField == txtPw) {
				grid.add(first, 0, 0);
				grid.add(first1, 1, 0);
				grid.add(second, 0, 1);
				grid.add(second1, 1, 1);
				grid.add(third, 0, 2);
				grid.add(third1, 1, 2);
				grid.add(fourth, 0, 3);
				grid.add(fourth1, 1, 3);
			}

			AnchorPane mainLayout = new AnchorPane();
			mainLayout.getChildren().addAll(grid, closePane);
			AnchorPane.setTopAnchor(closePane, 5.0);
			AnchorPane.setRightAnchor(closePane, 5.0);
			Scene scene = new Scene(mainLayout, 350, 150);
			validationPopup.setScene(scene);
			validationPopup.setX(900);
			validationPopup.setY(300);
			validationPopup.show();
		} else {
			// If the popup already exists, we just update the contents based on the field
			// clicked
			GridPane grid = (GridPane) ((AnchorPane) validationPopup.getScene().getRoot()).getChildren().get(0);
			grid.getChildren().clear(); // Clear the existing grid content

			if (sourceField == txtId) {
				grid.add(iconCharStart, 0, 0);
				grid.add(lblCharStart, 1, 0);
				grid.add(iconAlphaPart, 0, 1);
				grid.add(lblAlphaPart, 1, 1);
				grid.add(iconDigitPart, 0, 2);
				grid.add(lblDigitPart, 1, 2);
			} else if (sourceField == txtPw) {
				grid.add(first, 0, 0);
				grid.add(first1, 1, 0);
				grid.add(second, 0, 1);
				grid.add(second1, 1, 1);
				grid.add(third, 0, 2);
				grid.add(third1, 1, 2);
				grid.add(fourth, 0, 3);
				grid.add(fourth1, 1, 3);
			}

			validationPopup.show();
		}
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
	public static boolean isValidPhoneNumber(String phoneNumber) {
	    return phoneNumber.matches("09\\d{9}");
	}
	public static boolean isValidUserID(String input) {
	    return input.matches("A[A-Za-z]+\\d{7}");
	}

	public static boolean isValidGmail(String email) {
		return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,}$");
	}
	
	private void validatePhonePrefix(ComboBox<String> cboPhone) {
	    String selected = cboPhone.getValue();
	    if (selected != null && selected.equals("+95")) {
	        cboPhone.setStyle("-fx-border-color: green; -fx-text-fill: green;");
	    } else {
	        cboPhone.setStyle("-fx-border-color: red; -fx-text-fill: red;");
	    }
	}


	private boolean duplicateCheck() throws ClassNotFoundException, SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		try (PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM admin WHERE admin_id = ?")) {
			pst.setString(1, txtId.getText());
			try (ResultSet rs = pst.executeQuery()) {
				return rs.next() && rs.getInt(1) > 0;
			}
		}
	}

	@FXML
	void btnBrowseAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Image");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			photoImgView.setImage(new Image(selectedFile.toURI().toString()));
		}
	}

	private void saveAdminData() throws ClassNotFoundException, SQLException, IOException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		String gender = optMale.isSelected() ? "Male" : "Female";
		Date sqlDate = Date.valueOf(dpDOB.getValue());

		if (selectedFile == null) {
			throw new IOException("No image selected.");
		}
		if (Files.size(Paths.get(selectedFile.getAbsolutePath())) > IMAGE_SIZE_LIMIT) {
			throw new IOException("Image file exceeds the size limit of 300KB.");
		}

		try (PreparedStatement pst = con.prepareStatement(
				"INSERT INTO admin (admin_id, name, email, password, address, phone, gender, date_of_birth, photo, size) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
			pst.setString(1, txtId.getText());
			pst.setString(2, txtName.getText());
			pst.setString(3, txtEmail.getText());
			pst.setString(4, txtPw.getText());
			pst.setString(5, tetTfield.getText());
			pst.setString(6, cboPhone.getValue() + " " + txtPhone.getText());
			pst.setString(7, gender);
			pst.setDate(8, sqlDate);
			pst.setBinaryStream(9, new FileInputStream(selectedFile),
					(int) Files.size(Paths.get(selectedFile.getAbsolutePath())));
			pst.setLong(10, Files.size(Paths.get(selectedFile.getAbsolutePath())));
			pst.executeUpdate();

		}
		RegisterationForm.rForm.close();
	}

	private void clearFields() {
		txtId.clear();
		txtName.clear();
		txtEmail.clear();
		txtPhone.clear();
		cboPhone.setValue(null);
		genderToggleGroup.selectToggle(null);
		dpDOB.setValue(null);
		txtPw.clear();
		conpas.clear();
		tetTfield.clear();
		photoImgView.setImage(null);
		selectedFile = null;
	}

	private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
}
