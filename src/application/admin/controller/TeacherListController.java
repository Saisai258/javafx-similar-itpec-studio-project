package application.admin.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import application.main.DatabaseConnection;
import application.teacher.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TeacherListController implements Initializable {

	@FXML
	private TableColumn<Teacher, String> tGender;

	@FXML
	private TableColumn<Teacher, String> tID;

	@FXML
	private ComboBox<String> cboPhonePrefix;

	@FXML
	private TableColumn<Teacher, String> tPhone;

	@FXML
	private TableColumn<Teacher, String> tNo;

	@FXML
	private TableColumn<Teacher, String> tDOB;

	@FXML
	private TableView<Teacher> TeacherTableView;

	@FXML
	private TableColumn<Teacher, String> tName;

	@FXML
	private TableColumn<Teacher, String> tAddress;

	@FXML
	private TableColumn<Teacher, String> tEmail;

	@FXML
	private TableColumn<Teacher, String> tspecialism;
	
	private ObservableList<Teacher> list;

	@FXML
	private TextField txtAddress;

	@FXML
	private TextField txtTeacherID;

	@FXML
	private RadioButton optMale;

	@FXML
	private DatePicker dpDOB;

	@FXML
	private TextField txtEmail;

	@FXML
	private ImageView tImage;

	@FXML
	private TextField txtPhone;

	@FXML
	private TextField txtSpecialism;

	@FXML
	private TextField txtName;

	@FXML
	private RadioButton optFemale;
	
	@FXML
	private Button btnsearch;

	@FXML
	private TextField txtfield;

	private ToggleGroup genderToggleGroup;
	private String id, iD;
	File selected;
	int ides;

	public String[] patternCheck(String data, String splitSymbol) {
		String[] phone = new String[2];
		phone = data.split(splitSymbol);
		return phone;
	}

	@FXML
	private List<File> TeacherImages = new ArrayList<>();

	@FXML
	void handleTeacherMouseAction(MouseEvent event) throws SQLException {
		Teacher t = TeacherTableView.getSelectionModel().getSelectedItem();
		if (t != null) {
			txtTeacherID.setText(t.getTeacher_id());
			txtName.setText(t.getName());
			txtEmail.setText(t.getEmail());
//            txtPhone.setText(t.getPhone());
			String phone = t.getPhone();
			String[] prefixArray = patternCheck(phone, " ");
			cboPhonePrefix.setValue(prefixArray[0]);
			txtPhone.setText(prefixArray[1]);

			Date sqlDate = t.getDOB();
			LocalDate formDate = sqlDate.toLocalDate();
			dpDOB.setValue(formDate);
			txtAddress.setText(t.getAddress());
			if (t.getGender() != null) {
				if (t.getGender().equalsIgnoreCase("Male")) {
					optMale.setSelected(true);
				} else if (t.getGender().equalsIgnoreCase("Female")) {
					optFemale.setSelected(true);
				}
			} else {
				System.out.println("Gender is null.");
				optMale.setSelected(false);
				optFemale.setSelected(false);
			}
			txtSpecialism.setText(t.getSpecialism());

			id = t.getTeacher_id();
			ides = t.getIdes();
			System.out.println("Teacher ID: " + id);
			imageGet();
			updateStaticId();
		}

	}

	private void showTeacherList() throws SQLException {
		// TODO Auto-generated method stub

		tNo.setCellValueFactory(new PropertyValueFactory<>("id"));
		tID.setCellValueFactory(new PropertyValueFactory<>("teacher_id"));
		tName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
		tDOB.setCellValueFactory(new PropertyValueFactory<>("DOB"));
		tAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		tGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		tspecialism.setCellValueFactory(new PropertyValueFactory<>("specialism"));
		TeacherTableView.setItems(getTeacherlistAll());
	}

	private ObservableList<Teacher> getTeacherlistAll() throws SQLException {
		list = FXCollections.observableArrayList();
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		PreparedStatement pst = con.prepareStatement("select * from teacher");

		ResultSet rs = pst.executeQuery();
		int i = 0;
		while (rs.next()) {
			i++;
			int id = i;
			int ides = rs.getInt("id");
			iD = rs.getString("teacher_id");
			String tID = rs.getString("teacher_id");
			String tName = rs.getString("name");
			String tEmail = rs.getString("email");
			String tPhone = rs.getString("phone");
			Date tDOB = rs.getDate("date_of_birth");
			String tAddress = rs.getString("address");
			String tGender = rs.getString("gender");
			String tspecialism = rs.getString("specialism");

			Teacher t = new Teacher(id, ides, tID, tName, tEmail, tPhone, tDOB, tAddress, tGender, tspecialism);
			list.addAll(t);

		}
		return list;
	}

	public void imageGet() throws SQLException {

		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		PreparedStatement pst1 = con.prepareStatement("select photo from teacher where teacher_id=?");
		pst1.setString(1, id);
		ResultSet rs1 = pst1.executeQuery();
		if (rs1.next()) {
			byte[] imageData = rs1.getBytes(1);
			Image image = new Image(new ByteArrayInputStream(imageData));
			tImage.setImage(image);
		}
		System.out.println("HERE WE ARE" + id);
		if (tImage != null) {
			System.out.println("Image have");
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		genderToggleGroup = new ToggleGroup();
		optMale.setToggleGroup(genderToggleGroup);
		optFemale.setToggleGroup(genderToggleGroup);

		String[] phonePrefix = { "+95", "+44", "+1", "+91" };
		ObservableList<String> items = FXCollections.observableArrayList(phonePrefix);
		cboPhonePrefix.getItems().addAll(items);
		try {
			showTeacherList();
			updateStaticId();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		txtfield.textProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal.trim().isEmpty()) {
				TeacherTableView.setItems(list); // Reset table when search is empty
			}

		});
	}
	@FXML
	private void btnsearchAction() {
		String searchText = txtfield.getText().trim().toLowerCase();

		if (searchText.isEmpty()) {
			TeacherTableView.setItems(list); // Show all data if search is empty
			return;
		}

		FilteredList<Teacher> filteredData = new FilteredList<>(list,
				teacher ->teacher.getTeacher_id().toLowerCase().contains(searchText)
				||teacher.getGender().toLowerCase().contains(searchText)
				||teacher.getAddress().toLowerCase().contains(searchText)
				||teacher.getSpecialism().toLowerCase().contains(searchText));

		TeacherTableView.setItems(filteredData);
	}

	public char checkSpecialism() {
	    String specialism = txtSpecialism.getText();

	    if (specialism != null && !specialism.isEmpty()) {
	    	 System.out.println(specialism.toUpperCase().charAt(0));
	        return specialism.toUpperCase().charAt(0);
	       
	    } else {
	        // You can return a default character, or throw a custom exception, or just log and handle gracefully.
	        System.out.println("Specialism is empty or null.");
	        
	        return 'X'; // or handle another way
	    }
	}


	public void updateStaticId() {
	    String specialism = txtSpecialism.getText();
	    
	    if (iD != null && iD.length() > 2 && specialism != null && !specialism.isEmpty()) {
	        char newChar = Character.toUpperCase(specialism.charAt(0));
	        StringBuilder sb = new StringBuilder(iD);
	        
	        if (sb.charAt(1) != newChar) { // Only update if different
	            sb.setCharAt(1, newChar);
	            iD = sb.toString();
	            System.out.println("Updated teacher_id to: " + iD);
	        }
	    } else {
	        System.out.println("Cannot update teacher_id: missing or invalid specialism/ID.");
	    }
	}



	@FXML
	void updateAction(ActionEvent event) throws SQLException, IOException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement(
				"update teacher set name=?, email=?, address=?, gender=?, date_of_birth=?, specialism=?,  phone=?, photo=?, size=?, teacher_id=? where id=?");

		pst.setString(1, txtName.getText());
		pst.setString(2, txtEmail.getText());
		pst.setString(3, txtAddress.getText());
		String gender = null;
		if (optMale.isSelected()) {
			gender = optMale.getText();
		}
		if (optFemale.isSelected()) {
			gender = optFemale.getText();
		}
		pst.setString(4, gender);
		LocalDate selectedDate = dpDOB.getValue();
		Date sqlDate = Date.valueOf(selectedDate);
		pst.setDate(5, sqlDate);
		pst.setString(6, txtSpecialism.getText());
		pst.setString(7, cboPhonePrefix.getValue() + " " + txtPhone.getText());

		Image fxImage = tImage.getImage();

		if (fxImage != null) {
		    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);

		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ImageIO.write(bufferedImage, "png", baos); // or "jpg" if appropriate
		    byte[] imageBytes = baos.toByteArray();

		    if (imageBytes.length < 600000) {
		        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
		        pst.setBinaryStream(8, bais, imageBytes.length);
		        pst.setFloat(9, imageBytes.length);
		    } else {
		        Alert alert = new Alert(AlertType.WARNING);
		        alert.setTitle("Image Too Large");
		        alert.setContentText("Your image file size is too large. Please choose an image under 300 KB.");
		        alert.show();
		        return;
		    }
		} else {
		    Alert alert = new Alert(AlertType.WARNING);
		    alert.setTitle("No Image Found");
		    alert.setContentText("Please select or load an image before updating.");
		    alert.show();
		    return;
		}
		updateStaticId();
		pst.setString(10, iD);
		pst.setInt(11, ides);

		pst.executeUpdate();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Update Data");
		alert.setContentText("Successful Update data!");
		alert.show();
		txtTeacherID.clear();
		txtName.clear();
		txtEmail.clear();
		txtPhone.clear();
		txtAddress.clear();
		cboPhonePrefix.setValue(null);
		genderToggleGroup.selectToggle(null);
		dpDOB.setValue(null);
		tImage.setImage(null);
		selected = null;
		showTeacherList();
		
	}
	@FXML
	void btnDelete(ActionEvent event) throws SQLException {
	    String teacherId = txtTeacherID.getText();

	    if (teacherId == null || teacherId.isEmpty()) {
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("Warning");
	        alert.setHeaderText("No Teacher ID Provided");
	        alert.setContentText("Please select a teacher before attempting to delete.");
	        alert.show();
	        return;
	    }

	    DatabaseConnection db = new DatabaseConnection();
	    try (Connection con = db.getConnetion()) {
	        // Check if action is 0 in teacherinformation table
	        String checkQuery = "SELECT action FROM teacherinformation WHERE teach_id = ?";
	        try (PreparedStatement pst1 = con.prepareStatement(checkQuery)) {
	            pst1.setString(1, teacherId);
	            ResultSet rs = pst1.executeQuery();

	            if (rs.next()) {
	                int action = rs.getInt("action");
	                if (action == 0) {
	                    // Delete from teacher table
	                    String deleteQuery = "DELETE FROM teacher WHERE teacher_id = ?";
	                    try (PreparedStatement pstDelete = con.prepareStatement(deleteQuery)) {
	                        pstDelete.setString(1, teacherId);
	                        int rowsAffected = pstDelete.executeUpdate();
	                        
	                        String deleteInfo= "delete from teacherinformation where teach_id=?";
	                        PreparedStatement pstInfoDelete =con.prepareStatement(deleteInfo);
	                        pstInfoDelete.setString(1, teacherId);
	                        pstInfoDelete.executeUpdate();
	                        
	                        if (rowsAffected > 0) {
	                            clearFields();
	                            showTeacherList();
	                            Alert alert = new Alert(AlertType.INFORMATION);
	                            alert.setTitle("Delete Successful");
	                            alert.setHeaderText(null);
	                            alert.setContentText("Teacher information has been deleted successfully.");
	                            alert.show();
	                        } else {
	                            Alert alert = new Alert(AlertType.WARNING);
	                            alert.setTitle("Delete Failed");
	                            alert.setHeaderText(null);
	                            alert.setContentText("Teacher not found in the database.");
	                            alert.show();
	                        }
	                    }
	                } else {
	                    Alert alert = new Alert(AlertType.WARNING);
	                    alert.setTitle("Delete Blocked");
	                    alert.setHeaderText("This account cannot be deleted.");
	                    alert.setContentText("The teacher's action status does not permit deletion.");
	                    alert.show();
	                }
	            } else {
	                Alert alert = new Alert(AlertType.WARNING);
	                alert.setTitle("Not Found");
	                alert.setHeaderText("No Record Found");
	                alert.setContentText("No teacher information found with the given ID.");
	                alert.show();
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Database Error");
	        alert.setHeaderText("An error occurred");
	        alert.setContentText("Unable to complete the delete operation.");
	        alert.show();
	    }
	}

	// Helper method to clear fields
	private void clearFields() {
	    txtTeacherID.clear();
	    txtName.clear();
	    txtEmail.clear();
	    txtPhone.clear();
	    txtAddress.clear();
	    cboPhonePrefix.setValue(null);
	    genderToggleGroup.selectToggle(null);
	    dpDOB.setValue(null);
	    tImage.setImage(null);
	    selected = null;
	}


}
