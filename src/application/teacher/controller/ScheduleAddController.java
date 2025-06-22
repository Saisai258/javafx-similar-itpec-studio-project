package application.teacher.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.main.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class ScheduleAddController implements Initializable {

	@FXML
	private ComboBox<String> comboMin;

	@FXML
	private RadioButton radioPm;

	@FXML
	private TextField txtMarks;

	@FXML
	private ComboBox<String> comboChName;

	@FXML
	private TextField txtDesc;
	@FXML
	private TextField txtNewChapter;

	@FXML
	private ComboBox<String> comboHour;

	@FXML
	private RadioButton radioAM;

	@FXML
	private TextField txtExNO;

	@FXML
	private RadioButton radioFeAm;

	@FXML
	private RadioButton radioFePm;

	@FXML
	private RadioButton radioIP;

	@FXML
	private DatePicker exDate;

	@FXML
	private TextField txtBatch;

	@FXML
	private ToggleGroup timeToggleGroup;

	@FXML
	private ToggleGroup classToggleGroup;

	private String classes = "";
	private int examId;
	List<Integer> chapterId = new ArrayList<>();
	List<String> chapterList = new ArrayList<>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Toggle group setup
		timeToggleGroup = new ToggleGroup();
		radioAM.setToggleGroup(timeToggleGroup);
		radioPm.setToggleGroup(timeToggleGroup);

		classToggleGroup = new ToggleGroup();
		radioIP.setToggleGroup(classToggleGroup);
		radioFeAm.setToggleGroup(classToggleGroup);
		radioFePm.setToggleGroup(classToggleGroup);

		// Populate comboHour
		String[] hourPrefix = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
		comboHour.setItems(FXCollections.observableArrayList(hourPrefix));

		// Populate comboMin
		String[] minutePrefix = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13",
				"14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
				"31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47",
				"48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" };
		comboMin.setItems(FXCollections.observableArrayList(minutePrefix));

		action();
	}

	private void action() {

		radioIP.setOnAction(event -> {
			classes = "IP";
			examId = 1;
			comboChName.setValue(null);
			chapterId.clear();
			chapterList.clear();
			getChapterId();

		});

		radioFeAm.setOnAction(event -> {
			classes = "FE(AM)";
			examId = 2;
			comboChName.setValue(null);
			chapterId.clear();
			chapterList.clear();
			getChapterId();
		});

		radioFePm.setOnAction(event -> {
			classes = "FE(PM)";
			examId = 2;
			comboChName.setValue(null);
			chapterId.clear();
			chapterList.clear();
			getChapterId();
		});

	}

	private void getChapterId() {
		chapterId.clear(); // Clear old data

		DatabaseConnection db = new DatabaseConnection();
		try (Connection con = db.getConnetion();
				PreparedStatement pst = con.prepareStatement("SELECT c_id FROM chaptye WHERE exam_id = ?")) {

			pst.setInt(1, examId);

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					chapterId.add(rs.getInt("c_id"));
				}
			}

			// After collecting IDs, load their names
			loadChaptersFromDB();

		} catch (SQLException e) {
			e.printStackTrace();
			showError("Failed to Load Chapter IDs");
		}
	}

	private void loadChaptersFromDB() {
		if (chapterId.isEmpty()) {
			showError("No chapter IDs found for the selected class.");
			return;
		}

		// Build SQL with placeholders
		StringBuilder query = new StringBuilder("SELECT chapter_name FROM chapter WHERE c_id IN (");
		for (int i = 0; i < chapterId.size(); i++) {
			query.append("?");
			if (i < chapterId.size() - 1)
				query.append(",");
		}
		query.append(")");

		DatabaseConnection db = new DatabaseConnection();
		try (Connection con = db.getConnetion(); PreparedStatement pst = con.prepareStatement(query.toString())) {

			// Set parameters
			for (int i = 0; i < chapterId.size(); i++) {
				pst.setInt(i + 1, chapterId.get(i));
			}

			try (ResultSet rs = pst.executeQuery()) {
				chapterList.clear();
				while (rs.next()) {
					chapterList.add(rs.getString("chapter_name"));
				}
				comboChName.setItems(FXCollections.observableArrayList(chapterList));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			showError("Failed to Load Chapter Names");
		}
	}

	@FXML
	void btnScheduleAdd(ActionEvent event) {
		String option = "";
		if (radioAM.isSelected()) {
			option = "AM";
		} else if (radioPm.isSelected()) {
			option = "PM";
		}

		// Validate all required fields
		if (isNullOrEmpty(txtBatch.getText()) || isNullOrEmpty(txtExNO.getText())
				|| isNullOrEmpty(txtNewChapter.getText()) || isNullOrEmpty(txtDesc.getText())
				|| isNullOrEmpty(txtMarks.getText()) || exDate.getValue() == null || classes == null
				|| classes.isEmpty() || option.isEmpty() || comboChName.getValue() == null
				|| comboHour.getValue() == null || comboMin.getValue() == null) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Incomplete Data");
			alert.setHeaderText("Missing Required Fields");
			alert.setContentText("Please ensure all fields are properly filled.");
			alert.show();
			return;
		}

		// Save to database
		try {
			DatabaseConnection db = new DatabaseConnection();
			Connection con = db.getConnetion();
			PreparedStatement pst = con.prepareStatement(
					"INSERT INTO schedule (batch, class, chapter, description, exam_time, exam_date, question_no, marks) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

			pst.setString(1, txtBatch.getText().trim());
			pst.setString(2, classes);
			pst.setString(3, comboChName.getValue());
			pst.setString(4, txtDesc.getText().trim());
			pst.setString(5, comboHour.getValue() + ":" + comboMin.getValue() + " " + option);

			Date sqlDate = Date.valueOf(exDate.getValue());
			pst.setDate(6, sqlDate);

			pst.setString(7, txtExNO.getText().trim());
			pst.setString(8, txtMarks.getText().trim());

			pst.executeUpdate();
			pst.close();
			con.close();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText("Schedule Added");
			alert.setContentText("The schedule was added successfully.");
			alert.show();

			resetFields(); // Optional method to clear the form
			action(); // Reload or refresh UI

		} catch (SQLException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Database Error");
			alert.setHeaderText("Failed to Add Schedule");
			alert.setContentText("An error occurred while adding the schedule.");
			alert.show();
		}
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	private void resetFields() {
		txtBatch.clear();
		txtExNO.clear();
		txtNewChapter.clear();
		txtDesc.clear();
		txtMarks.clear();
		comboHour.getSelectionModel().clearSelection();
		comboMin.getSelectionModel().clearSelection();
		comboChName.getSelectionModel().clearSelection();
		exDate.setValue(null);
		timeToggleGroup.selectToggle(null);
		classToggleGroup.selectToggle(null);
	}

	private void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Database Error");
		alert.setHeaderText(message);
		alert.setContentText("Please contact administrator or retry.");
		alert.show();
	}

}
