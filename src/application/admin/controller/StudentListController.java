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
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import application.main.DatabaseConnection;
import application.student.Student;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class StudentListController implements Initializable {

	@FXML
	private TableView<Student> StudentTableView;

	private ObservableList<Student> list;
	@FXML
	private ComboBox<String> cboPhonePrefix;

	@FXML
	private TableColumn<Student, String> sAddress;

	@FXML
	private TableColumn<Student, String> sDOB;

	@FXML
	private TableColumn<Student, String> sEmail;

	@FXML
	private TableColumn<Student, String> sGender;

	@FXML
	private TableColumn<Student, String> sID;

	@FXML
	private TableColumn<Student, String> sName;

	@FXML
	private TableColumn<Student, String> sNo;

	@FXML
	private TableColumn<Student, String> sPhone;

	@FXML
	private TableColumn<Student, String> sBatch;

	@FXML
	private TableColumn<Student, String> sClass;

	@FXML
	private TextField txtAddress;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtPhone;

	@FXML
	private TextField txtStudentID;

	@FXML
	private TextField txtBatch;

	@FXML
	private TextField texClass;

	private ToggleGroup genderToggleGroup;

	@FXML
	private DatePicker dpDOB;

	@FXML
	private Hyperlink hello;

	@FXML
	private RadioButton optFemale;

	@FXML
	private RadioButton optMale;

	@FXML
	private ImageView sImage;

	@FXML
	private Button btnsearch;

	@FXML
	private TextField txtfield;
	private String id;
	File selected;

	public String[] patternCheck(String data, String splitSymbol) {
		String[] phone = new String[2];
		phone = data.split(splitSymbol);
		return phone;

	}

	@FXML
	void handleStudentMouseAction(MouseEvent event) throws SQLException {
		Student s = StudentTableView.getSelectionModel().getSelectedItem();
		if (s != null) {
			txtStudentID.setText(s.getStudent_id());
			txtName.setText(s.getName());
			txtEmail.setText(s.getEmail());
			txtPhone.setText(s.getPhone());
			String phone = s.getPhone();
			String[] prefixArray = patternCheck(phone, " ");
			cboPhonePrefix.setValue(prefixArray[0]);
			txtPhone.setText(prefixArray[1]);
			txtBatch.setText(s.getBatch());
			texClass.setText(s.getSclass());
			Date sqlDate = s.getDOB();
			LocalDate formDate = sqlDate.toLocalDate();
			dpDOB.setValue(formDate);
			txtAddress.setText(s.getAddress());
			if (s.getGender() != null) {
				if (s.getGender().equalsIgnoreCase("Male")) {
					optMale.setSelected(true);
				} else if (s.getGender().equalsIgnoreCase("Female")) {
					optFemale.setSelected(true);
				}
			} else {
				System.out.println("Gender is null.");
				optMale.setSelected(false);
				optFemale.setSelected(false);
			}
			id = s.getStudent_id();
			System.out.println("Student ID: " + id);
			imageGet();
		}
	}

	public void imageGet() throws SQLException {

		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		PreparedStatement pst1 = con.prepareStatement("select photo from student where student_id=?");
		pst1.setString(1, id);
		ResultSet rs1 = pst1.executeQuery();
		if (rs1.next()) {
			byte[] imageData = rs1.getBytes(1);
			Image image = new Image(new ByteArrayInputStream(imageData));
			sImage.setImage(image);
		}
		System.out.println("HERE WE ARE" + id);
		if (sImage != null) {
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
			showStudentList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		txtfield.textProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal.trim().isEmpty()) {
				StudentTableView.setItems(list); // Reset table when search is empty
			}

		});
	}

	@FXML
	private void btnsearchAction() {
		String searchText = txtfield.getText().trim().toLowerCase();

		if (searchText.isEmpty()) {
			StudentTableView.setItems(list); // Show all data if search is empty
			return;
		}

		FilteredList<Student> filteredData = new FilteredList<>(list,
				student -> student.getStudent_id().toLowerCase().contains(searchText)
						|| student.getName().toLowerCase().contains(searchText)
						|| student.getAddress().toLowerCase().contains(searchText)
						|| student.getBatch().toLowerCase().contains(searchText)
						|| student.getGender().toLowerCase().contains(searchText)
						|| student.getSclass().toLowerCase().contains(searchText));

		StudentTableView.setItems(filteredData);
	}

	private void showStudentList() throws SQLException {
		// TODO Auto-generated method stub

		sNo.setCellValueFactory(new PropertyValueFactory<>("id"));
		sID.setCellValueFactory(new PropertyValueFactory<>("student_id"));
		sName.setCellValueFactory(new PropertyValueFactory<>("name"));
		sEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		sPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
		sDOB.setCellValueFactory(new PropertyValueFactory<>("DOB"));
		sAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		sGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		sBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
		sClass.setCellValueFactory(new PropertyValueFactory<>("sclass"));
		StudentTableView.setItems(getStudentlistAll());
	}

	private ObservableList<Student> getStudentlistAll() throws SQLException {
		list = FXCollections.observableArrayList();
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		PreparedStatement pst = con.prepareStatement("select * from student");

		ResultSet rs = pst.executeQuery();
		int i = 0;
		while (rs.next()) {
			i++;
			int id = i;
			String sID = rs.getString("student_id");
			String sName = rs.getString("name");
			String sEmail = rs.getString("email");
			String sPhone = rs.getString("phone");
			Date sDOB = rs.getDate("date_of_birth");
			String sAddress = rs.getString("address");
			String sGender = rs.getString("gender");
			String sBatch = rs.getString("batch");
			String sClass = rs.getString("class");

			Student s = new Student(id, sID, sName, sEmail, sPhone, sDOB, sAddress, sGender, sBatch, sClass);
			list.add(s);
			System.out.println("HERE WE ARE");
		}
		return list;
	}

	@FXML
	void updateAction(ActionEvent event) throws SQLException, IOException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement(
				"update student set name=?, email=?, address=?, phone=?, gender=?, date_of_birth=?, batch=?, class=?, photo=?, size=? where student_id=?");
		pst.setString(1, txtName.getText());
		pst.setString(2, txtEmail.getText());
		pst.setString(3, txtAddress.getText());
		pst.setString(4, cboPhonePrefix.getValue() + " " + txtPhone.getText());
		String gender = null;
		if (optMale.isSelected()) {
			gender = optMale.getText();
		}
		if (optFemale.isSelected()) {
			gender = optFemale.getText();
		}
		pst.setString(5, gender);
		LocalDate selectedDate = dpDOB.getValue();
		Date sqlDate = Date.valueOf(selectedDate);
		pst.setDate(6, sqlDate);
		pst.setString(7, txtBatch.getText());
		pst.setString(8, texClass.getText());
		Image fxImage = sImage.getImage();

		if (fxImage != null) {
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", baos); // or "jpg" if appropriate
			byte[] imageBytes = baos.toByteArray();

			if (imageBytes.length < 600000) {
				ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
				pst.setBinaryStream(9, bais, imageBytes.length);
				pst.setFloat(10, imageBytes.length);
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
		pst.setString(11, txtStudentID.getText());

		pst.executeUpdate();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Update Data");
		alert.setContentText("Successful Update data!");
		alert.show();
		txtStudentID.clear();
		txtName.clear();
		txtEmail.clear();
		txtPhone.clear();
		txtAddress.clear();
		cboPhonePrefix.setValue(null);
		genderToggleGroup.selectToggle(null);
		dpDOB.setValue(null);
		sImage.setImage(null);
		selected = null;
		txtBatch.clear();
		texClass.clear();
		showStudentList();
	}

	@FXML
	void btnDelete(ActionEvent event) throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		// Step 1: Delete exam results first to satisfy foreign key constraint
		String deleteResultsSql = "DELETE FROM examresult WHERE student_id = ?";
		PreparedStatement ps1 = con.prepareStatement(deleteResultsSql);
		ps1.setString(1, txtStudentID.getText());
		ps1.executeUpdate();

		// Step 2: Delete student record
		PreparedStatement pst = con.prepareStatement("DELETE FROM student WHERE student_id = ?");
		pst.setString(1, txtStudentID.getText());
		pst.executeUpdate();

		// Show success alert
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Delete Data");
		alert.setContentText("Deleted this user's information from Database!");
		alert.show();

		// Clear fields
		txtStudentID.clear();
		txtName.clear();
		txtEmail.clear();
		txtPhone.clear();
		txtAddress.clear();
		cboPhonePrefix.setValue(null);
		genderToggleGroup.selectToggle(null);
		dpDOB.setValue(null);
		sImage.setImage(null);
		selected = null;
		txtBatch.clear();
		texClass.clear();

		// Refresh list
		showStudentList();
	}
}
