package application.teacher.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import application.admin.form.TeacherInformationForm;
import application.main.DatabaseConnection;
import application.teacher.form.TeacherLoginForm;
import application.teacher.form.TeacherRegisterationform;
import javafx.animation.PauseTransition;
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
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
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
import javafx.util.Duration;

public class TeacherRegisterationFormController implements Initializable {

    @FXML private PasswordField txtpassword1, txtpassword2;
    @FXML private ComboBox<String> cbophone;
    @FXML private TextField txtemail, txtname, txtspecial, txtphone, txtaddress;
    @FXML private DatePicker dbDOB;
    @FXML private RadioButton optmale, optfemale;
    @FXML private ImageView photoImgView;

    private ToggleGroup genderToggleGroup;
    public static String getId;
    private File selectedFile;
    public static char str;
    private Stage validationPopup;
    private final Label first = new Label("\u2716"), second = new Label("\u2716"),
                         third = new Label("\u2716"), fourth = new Label("\u2716");
    private final Label first1 = new Label("UPPER LETTER.!"), second1 = new Label("LOWER LETTER.!"),
                         third1 = new Label("DIGITS.!"), fourth1 = new Label("SPECIAL CHARACTER.!");

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        method();
    }

    public void method() {
        genderToggleGroup = new ToggleGroup();
        optmale.setToggleGroup(genderToggleGroup);
        optfemale.setToggleGroup(genderToggleGroup);

        ObservableList<String> items = FXCollections.observableArrayList("+95", "+1", "+44", "+61", "+91");
        cbophone.setItems(items);

        txtpassword1.setOnMouseClicked(event -> createValidationPopup());
        txtpassword1.textProperty().addListener((obs, oldVal, newVal) -> checkPasswordStrength(newVal));

        addClosePopupListener(txtname);
        addClosePopupListener(txtemail);
        addClosePopupListener(txtphone);
        addClosePopupListener(txtaddress);
        addClosePopupListener(optmale);
        addClosePopupListener(optfemale);
        addClosePopupListener(dbDOB);
        addClosePopupListener(cbophone);
        addClosePopupListener(txtpassword2);
        addClosePopupListener(txtspecial);
    }

    private void addClosePopupListener(Control control) {
        if (control instanceof TextField || control instanceof ComboBox) {
            control.setOnMouseClicked(event -> closeValidationPopup());
        } else if (control instanceof RadioButton) {
            ((RadioButton) control).setOnMouseClicked(event -> closeValidationPopup());
        } else if (control instanceof DatePicker) {
            ((DatePicker) control).showingProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal) closeValidationPopup();
            });
        }
    }

    private void closeValidationPopup() {
        if (validationPopup != null && validationPopup.isShowing()) {
            validationPopup.close();
        }
    }

    private void checkPasswordStrength(String password) {
        updateValidationIcon(first, first1, password.matches(".*[A-Z].*"));
        updateValidationIcon(second, second1, password.matches(".*[a-z].*"));
        updateValidationIcon(third, third1, password.matches(".*\\d.*"));
        updateValidationIcon(fourth, fourth1, password.matches(".*[!@#$%^&*].*"));
    }

    private void updateValidationIcon(Label icon, Label label, boolean isValid) {
        icon.setText(isValid ? "\u2714" : "\u2716");
        String color = isValid ? "green" : "red";
        icon.setStyle("-fx-text-fill: " + color);
        label.setStyle("-fx-text-fill: " + color);
    }

    private void createValidationPopup() {
        if (validationPopup == null) {
            validationPopup = new Stage();
            Button closeButton = new Button("\u2718");
            closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px;");
            closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-text-fill: red; -fx-font-size: 18px;"));
            closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-text-fill: black; -fx-font-size: 16px;"));
            closeButton.setOnAction(e -> validationPopup.close());

            StackPane closePane = new StackPane(closeButton);
            closePane.setAlignment(Pos.TOP_RIGHT);
            closePane.setPadding(new Insets(5));

            GridPane grid = new GridPane();
            grid.addRow(0, first, first1);
            grid.addRow(1, second, second1);
            grid.addRow(2, third, third1);
            grid.addRow(3, fourth, fourth1);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(10));

            AnchorPane layout = new AnchorPane(grid, closePane);
            AnchorPane.setTopAnchor(closePane, 5.0);
            AnchorPane.setRightAnchor(closePane, 5.0);
            validationPopup.setScene(new Scene(layout, 350, 150));
            validationPopup.initStyle(StageStyle.UNDECORATED);
            validationPopup.setAlwaysOnTop(true);
            validationPopup.setX(900);
			validationPopup.setY(300);
        }
        validationPopup.show();
    }

    @FXML
    void photoBrowse(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG"),
            new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG")
        );
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            photoImgView.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    @FXML
    void loginLinkAction(ActionEvent event) throws IOException {
        new TeacherRegisterationform().toggleTeacherRegistrationForm();
        new TeacherLoginForm().toggleTeacherLoginForm();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    public static boolean isValidPhoneNumber(String number) {
        return number.matches("\\d+");
    }

    @FXML
    private void btnRegisterAction(ActionEvent event) throws Exception {
        if (txtname.getText().isEmpty() || txtemail.getText().isEmpty() || dbDOB.getValue() == null ||
            txtspecial.getText().isEmpty() || cbophone.getValue() == null || txtphone.getText().isEmpty() ||
            txtpassword1.getText().isEmpty() || txtpassword2.getText().isEmpty() ||
            !(optmale.isSelected() ^ optfemale.isSelected())) {

            showAlert(Alert.AlertType.WARNING, "Required Data", "Fill all required data!", "All fields are mandatory.");
            return;
        }

        String email = txtemail.getText();
        txtemail.setText(email);
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,}$")) {
            showAlert(Alert.AlertType.WARNING, "Invalid Email", "Email must end with '@gmail.com'", "Please correct it.");
            return;
        }

        if (!isValidPhoneNumber(txtphone.getText()) || txtphone.getLength() != 11) {
            showAlert(Alert.AlertType.ERROR, "Invalid Phone", "Phone must be 11 digits only.", "Please correct it.");
            return;
        }

        if (dbDOB.getValue().isAfter(LocalDate.now().minusYears(18))) {
            showAlert(Alert.AlertType.ERROR, "Invalid Date of Birth", "Must be at least 18 years old.", "Correct your DOB.");
            return;
        }

        if (txtpassword1.getLength() < 8) {
            showAlert(Alert.AlertType.WARNING, "Password Too Short", "Minimum 8 characters required.", "Strengthen your password.");
            return;
        }

        if (!txtpassword1.getText().equals(txtpassword2.getText())) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match.", "Please check again.");
            return;
        }

        if (duplicateCheck()) {
            showAlert(Alert.AlertType.WARNING, "Duplicate Entry", "Email already exists.", "Try with another email.");
            return;
        }

        idFormat();
        TeacherNumberGet();
        saveteacherdata();
        closeValidationPopup();
    }

    private boolean duplicateCheck() throws SQLException, ClassNotFoundException {
        try (Connection con = new DatabaseConnection().getConnetion();
             PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM teacher WHERE email = ?")) {
            pst.setString(1, txtemail.getText());
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public void idFormat() throws SQLException, ClassNotFoundException {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        int seconds = time.getSecond();
        String year = Integer.toString(date.getYear());
        String formattedMonth = String.format("%02d", date.getMonthValue());
		String formattedSecond = String.format("%02d", seconds);
        int id = TeacherNumberGet();
        str = txtspecial.getText().toUpperCase().charAt(0);
        String formattedid = String.format("%02d", id);
        getId = "M" + str + "-" + year + formattedMonth+formattedSecond + formattedid;
    }

    public int TeacherNumberGet() throws SQLException {
        try (Connection con = new DatabaseConnection().getConnetion();
             PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) AS total_rows FROM teacher");
             ResultSet rs = pst.executeQuery()) {
            return rs.next() ? rs.getInt("total_rows") + 1 : 1;
        }
    }

    private void saveteacherdata() throws Exception {
        try (Connection con = new DatabaseConnection().getConnetion();
             PreparedStatement pst = con.prepareStatement(
                "INSERT INTO teacher (teacher_id, name, email, address, phone, gender, date_of_birth, specialism, password, photo, size) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            pst.setString(1, getId);
            pst.setString(2, txtname.getText());
            pst.setString(3, txtemail.getText());
            pst.setString(4, txtaddress.getText());
            pst.setString(5, cbophone.getValue() + " " + txtphone.getText());
            pst.setString(6, optmale.isSelected() ? "Male" : "Female");
            pst.setDate(7, Date.valueOf(dbDOB.getValue()));
            pst.setString(8, txtspecial.getText());
            pst.setString(9, txtpassword1.getText());

            if (selectedFile != null) {
                Path path = selectedFile.toPath();
                long size = Files.size(path);
                if (size > 300000) {
                    showAlert(Alert.AlertType.WARNING, "File Error", "File size too large", "Must be < 300KB.");
                    return;
                }
                pst.setBinaryStream(10, new FileInputStream(selectedFile), selectedFile.length());
                pst.setFloat(11, size);
            } else {
                pst.setBinaryStream(10, null);
                pst.setFloat(11, 0);
            }
            pst.executeUpdate();
        }

        clearFields();
        
       new TeacherRegisterationform().toggleTeacherRegistrationForm();
        
		TeacherInformationForm tinfo = new TeacherInformationForm();
		// To open the form
		tinfo.showForm();
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("notification!!!");
		alert.setHeaderText("Please enter the required teacher details to grant full system access.!");
		alert.setContentText("Thank you!.");
		alert.show();
		PauseTransition delay = new PauseTransition(Duration.seconds(10));
		delay.setOnFinished(e -> alert.close());
		delay.play();

    }

    private void clearFields() {
        txtname.clear();
        txtemail.clear();
        txtaddress.clear();
        txtphone.clear();
        cbophone.setValue(null);
        dbDOB.setValue(null);
        txtpassword1.clear();
        txtpassword2.clear();
        txtspecial.clear();
        photoImgView.setImage(null);
        genderToggleGroup.selectToggle(null);
    }
}
