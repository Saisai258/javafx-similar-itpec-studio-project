package application.student.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import application.main.DatabaseConnection;
import application.student.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.HBox;

public class ContactUsController {

    @FXML
    private TextArea txtComment;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private HBox centerBar;

    private Reflection reflection; // Declare Reflection object
	Student logStu = SignInController.login_student;

    @FXML
    public void initialize() {
    	txtEmail.setText(logStu.getEmail());
        reflection = new Reflection(); // Initialize the Reflection effect
        reflection.setTopOpacity(0.5); // Set the opacity of the reflection
        reflection.setBottomOpacity(0); // Make the bottom part of the reflection fade out
        reflection.setFraction(0.7); // Control the size of the reflected area

        centerBar.setEffect(reflection); // Apply the reflection effect to the HBox
    }
   
    @FXML
    void SubmitAction(ActionEvent event) {
        try {
            if (txtFirstName.getText().isEmpty() || 
                txtLastName.getText().isEmpty() || 
                txtEmail.getText().isEmpty() || 
                txtComment.getText().isEmpty()) {
                
                showAlert(Alert.AlertType.WARNING, "Required Data", "Fill all required data!", "Please fill in all required fields.");
            
            }else if(!txtEmail.getText().toLowerCase().endsWith("@gmail.com")) { 
                showAlert(Alert.AlertType.WARNING, "Invalid Email", "Invalid Email Format", "Please use an email ending with '@gmail.com'.");
            
            } else {
            	addData();
            	clearData();
            }

        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.", "Please try again later.");
            ex.printStackTrace();
        }
    }
    public void clearData() {
    	txtFirstName.clear();
        txtLastName.clear();
        txtEmail.clear();
        txtComment.clear();
    }

    private void addData() throws SQLException {
    	
    	DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		PreparedStatement pst = con.prepareStatement("insert into contact(username,usergmail,description,date) value(?,?,?,?)");
		pst.setString(1, txtFirstName.getText()+" "+txtLastName.getText());
		pst.setString(2, txtEmail.getText());
		pst.setString(3, txtComment.getText());
		LocalDate selectedDate = LocalDate.now();
		Date sqlDate = Date.valueOf(selectedDate);
		pst.setDate(4, sqlDate);
		pst.executeUpdate();
		
		showAlert(Alert.AlertType.INFORMATION, "Submission Successful", "Submit", "Thank you for your submission!");
    }
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}

