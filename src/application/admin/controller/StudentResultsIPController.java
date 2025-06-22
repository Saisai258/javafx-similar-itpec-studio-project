package application.admin.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.teacher.ExamResult;
import application.admin.form.StudentResultsFEForm;
import application.admin.form.StudentResultsIPForm;
import application.main.DatabaseConnection;
import application.student.Student;
import application.teacher.form.TeacherHomeForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class StudentResultsIPController implements Initializable {

	private int selectedEID;

	private ObservableList<ExamResult> examResultsList;

	@FXML
	private TextField txtfield;

	@FXML
	private TextField txtid;

	@FXML
	private TextField txtbatch;

	@FXML
	private TextField txtclass;

	@FXML
	private TextField txtmark;

	@FXML
	private TableColumn<Student, Integer> sno;

	@FXML
	private TextField txtname;

	@FXML
	private TableColumn<Student, String> sname;

	@FXML
	private TableColumn<Student, String> sbatch;

	@FXML
	private TableColumn<Student, String> sclass;

	@FXML
	private TableColumn<Student, String> smark;

	@FXML
	private Button btnupdate;

	@FXML
	private TableColumn<Student, String> sid;

	@FXML
	private TableView<Student> studentipViewTable;

	private ObservableList<Student> studentResultsList;

	@FXML
	private Button btnsearch;

	@FXML
	private TableColumn<ExamResult, String> edate;

	@FXML
	private TableView<ExamResult> studentrResultIpTable;

	@FXML
	private TableColumn<ExamResult, String> ename;

	@FXML
	private TableColumn<ExamResult, String> etotalquestion;

	@FXML
	private TableColumn<ExamResult, String> etype;

	@FXML
	private TableColumn<ExamResult, String> eid;

	@FXML
	private TextField txtexamname;

	@FXML
	private DatePicker txtexamdate;

	@FXML
	private ComboBox<String> comboexamtype;

	@FXML
	private TextField txttotalquestions;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		// Debugging: Check if eid is properly initialized
//		System.out.println("eid: " + eid);
//		System.out.println("comboexamtype: " + comboexamtype);
		try {

			// Load student results list
			studentResultsList = getStudentResultsListAll();
			showStudentResultsList();

			// Initialize the exam results list and bind it to the table
			examResultsList = FXCollections.observableArrayList();
			studentrResultIpTable.setItems(examResultsList);
			showExamResultsList();

			// Add listener for row selection in student table
			studentipViewTable.getSelectionModel().selectedItemProperty()
					.addListener((obs, oldSelection, newSelection) -> {
						if (newSelection != null) {
							try {
								showExamResults(newSelection.getStudent_id());
							} catch (SQLException e) {
								e.printStackTrace();
							}
						} else {
							// Clear exam results if no student is selected
							examResultsList.clear();
						}
					});

			txtfield.textProperty().addListener((obs, oldVal, newVal) -> {
				if (newVal.trim().isEmpty()) {
					studentipViewTable.setItems(studentResultsList); // Reset table when search is empty
				}

			});
			System.out.println(" I am Boy");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void btnsearchAction() {
		String searchText = txtfield.getText().trim().toLowerCase();

		if (searchText.isEmpty()) {
			studentipViewTable.setItems(studentResultsList); // Show all data if search is empty
			return;
		}

		FilteredList<Student> filteredData = new FilteredList<>(studentResultsList,
				student -> student.getStudent_id().toLowerCase().contains(searchText)
						|| student.getName().toLowerCase().contains(searchText));

		studentipViewTable.setItems(filteredData);
	}

	@FXML
	void btnipcanelAction(ActionEvent event) {
		studentipViewTable.getSelectionModel().clearSelection();

		txtname.clear();
		txtid.clear();
		txtbatch.clear();
		txtclass.clear();
		txtmark.clear();
		txtexamdate.setValue(null);
		txtexamname.clear();
		comboexamtype.setValue(null);
		txttotalquestions.clear();
	}
	@FXML
	void btnDelete(ActionEvent event) throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("delete from examresult where student_id=?");
		pst.setString(1, txtid.getText());
		pst.executeUpdate();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Delete Data");
		alert.setContentText("Deleted this Student ID of exam result!");
		alert.show();

		txtname.clear();
		txtid.clear();
		txtbatch.clear();
		txtclass.clear();
		txtmark.clear();
		txtexamdate.setValue(null);
		txtexamname.clear();
		comboexamtype.setValue(null);
		txttotalquestions.clear();
		
		getStudentResultsListAll();
		showExamResultsList();
	}

	@FXML
	void btnipupdateAction(ActionEvent event) throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		PreparedStatement pst = con.prepareStatement(
				"UPDATE examresult SET exam_date = ?, question_number = ?, exam_name = ?, exam_type = ?, total_mark = ? "
						+ "WHERE e_id = ?");

		if (txtexamdate.getValue() == null || txttotalquestions.getText().equals("") || txtexamname.getText().equals("")
				|| comboexamtype.getValue() == null || txtmark.getText().equals("")
				|| Integer.parseInt(txtmark.getText()) < 0 || selectedEID == -1) {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Required Or Incorrect Data");
			alert.setHeaderText("Fill all required data correctly!");
			alert.setContentText("Please ensure all required data is filled in correctly.");
			alert.show();
		} else {
			java.time.LocalDate localDate = txtexamdate.getValue();
			java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
			pst.setDate(1, sqlDate);

			pst.setInt(2, Integer.parseInt(txttotalquestions.getText()));
			pst.setString(3, txtexamname.getText());
			pst.setString(4, comboexamtype.getValue());
			pst.setInt(5, Integer.parseInt(txtmark.getText()));
			pst.setInt(6, selectedEID);

			int rowsAffected = pst.executeUpdate();
			
			PreparedStatement pst1 = con.prepareStatement(
	    	        "UPDATE student SET name = ?, batch = ?, class = ? WHERE student_id = ?"
	    	    );
	        pst1.setString(1, txtname.getText());
	        pst1.setString(2, txtbatch.getText());
	        pst1.setString(3, txtclass.getText());
	        pst1.setString(4, txtid.getText());
	        
	        pst1.executeUpdate();
	        
			if (rowsAffected > 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Update Data");
				alert.setContentText("Data updated successfully!");
				alert.show();
				
				getStudentResultsListAll();
				showExamResults(txtid.getText()); // Pass the student ID to reload exam results
				 clearExamResultsList();
		            txtname.clear();
		    		txtid.clear();
		    		txtbatch.clear();
		    		txtclass.clear();
		    		txtmark.clear();
		    		txtexamdate.setValue(null);
		    		txtexamname.clear();
		    		comboexamtype.setValue(null);
		    		txttotalquestions.clear();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Update Data");
				alert.setContentText("No data was updated. Please check the entered information.");
				alert.show();
			}
		}
	}

	@FXML
	void handleStudentIpMouseAction(MouseEvent event) {

		// Clear Exam Results text fields when clicking Student Table
		clearExamResultsList();

		Student s = (Student) studentipViewTable.getSelectionModel().getSelectedItem();
		if (s != null) {
			txtname.setText(s.getName());
			txtid.setText(s.getStudent_id());
			txtbatch.setText(s.getBatch());
			txtclass.setText(s.getSclass());

		} // Correct type conversion
		else { // Clear the text fields if no student is selected
			txtname.clear();
			txtid.clear();
			txtbatch.clear();
			txtclass.clear();

		}
	}

	private void clearExamResultsList() {
		txtexamdate.setValue(null);
		txtexamname.clear();
		comboexamtype.setValue(null);
		txttotalquestions.clear();
		txtmark.clear();

	}

	@FXML
	void handleIpExamResultsMouseAction(MouseEvent event) {
		ExamResult e = (ExamResult) studentrResultIpTable.getSelectionModel().getSelectedItem();
		if (e != null) {
			selectedEID = e.getE_id();

			txtmark.setText(String.valueOf(e.getMark()));
			txtexamdate.setValue(e.getExam_date().toLocalDate());
			txttotalquestions.setText(String.valueOf(e.getQuestion_number()));
			txtexamname.setText(e.getExam_name());
			comboexamtype.setValue(e.getExam_type());
		} // Correct type conversion
		else { // Clear the text fields if no student is selected
			selectedEID = -1;
			txtmark.clear();
			txtexamdate.setValue(null);
			txttotalquestions.clear();
			txtexamname.clear();
			comboexamtype.setValue(null);
		}
	}

	private void showStudentResultsList() throws SQLException {
		// TODO Auto-generated method stub
		sno.setCellValueFactory(new PropertyValueFactory<>("id"));
		sid.setCellValueFactory(new PropertyValueFactory<>("student_id"));
		sname.setCellValueFactory(new PropertyValueFactory<>("name"));
		sbatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
		sclass.setCellValueFactory(new PropertyValueFactory<>("sclass"));

		studentipViewTable.setItems(studentResultsList);

	}

	private void showExamResultsList() {
		// Set up the cell value factories for the TableView columns
		eid.setCellValueFactory(new PropertyValueFactory<>("student_id"));
		smark.setCellValueFactory(new PropertyValueFactory<>("mark"));
		edate.setCellValueFactory(new PropertyValueFactory<>("exam_date"));
		etotalquestion.setCellValueFactory(new PropertyValueFactory<>("question_number"));
		ename.setCellValueFactory(new PropertyValueFactory<>("exam_name"));
		etype.setCellValueFactory(new PropertyValueFactory<>("exam_type"));

		// Initialize the ComboBox for exam types (uncomment if needed)
		String[] examTypes = { "Monthly", "Practice"};
		ObservableList<String> items = FXCollections.observableArrayList(examTypes);
//	    comboexamtype.getItems().clear();
		comboexamtype.getItems().addAll(items);

		// Initialize the examResultsList and bind it to the TableView
		studentrResultIpTable.setItems(examResultsList);
	}

	private ObservableList<Student> getStudentResultsListAll() throws SQLException {
		// TODO Auto-generated method stub

		studentResultsList = FXCollections.observableArrayList();
		studentipViewTable.setItems(studentResultsList);
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		PreparedStatement pst = con
				.prepareStatement("SELECT DISTINCT student_id, name, batch, class FROM student WHERE class = 'IP'");
		ResultSet rs = pst.executeQuery();

		int i = 0;
		while (rs.next()) {
			i++;
			System.out.println(i);

			int id = i;
			String sid = rs.getString("student_id");
			String sname = rs.getString("name");
			String sbatch = rs.getString("batch");
			String sclass = rs.getString("class");

			Student s = new Student(id, sid, sname, sbatch, sclass);
			studentResultsList.add(s);
		}
		return studentResultsList;
	}

	private void showExamResults(String student_id) throws SQLException {
		// Clear previous data
		examResultsList.clear();

		// Establish database connection
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		String sql = "SELECT * FROM examresult WHERE student_id = ?";

		try (PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, student_id);
			System.out.println(student_id + " Student ID for result");

			try (ResultSet rs = pst.executeQuery()) { 
				while (rs.next()) {
					int eid = rs.getInt("e_id");
					String sid = rs.getString("student_id");
					Date edate = rs.getDate("exam_date");
					int etotalquestion = rs.getInt("question_number");
					String ename = rs.getString("exam_name");
					String etype = rs.getString("exam_type");
					int smark = rs.getInt("total_mark");

					ExamResult examResult = new ExamResult(eid, sid, edate, etotalquestion, ename, etype, smark);
					examResultsList.add(examResult);
				}
			}
		} finally {
			con.close();
		}

		// Update the TableView
		studentrResultIpTable.setItems(examResultsList);
	}

	@FXML
	void ipResultAction(ActionEvent event) throws IOException {
		StudentResultsIPForm ip = new StudentResultsIPForm();
		TeacherHomeForm.root.setCenter(ip.studentResultsIPForm());
	}

	@FXML
	void feResultAction(ActionEvent event) throws IOException {
		StudentResultsFEForm fe = new StudentResultsFEForm();
		TeacherHomeForm.root.setCenter(fe.studentResultsFEForm());
	}
}
