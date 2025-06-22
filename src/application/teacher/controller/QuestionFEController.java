package application.teacher.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.main.DatabaseConnection;
import application.teacher.form.QuestionFEForm;
import application.teacher.form.QuestionForm;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class QuestionFEController implements Initializable {
	@FXML
	private ComboBox<String> chapter;

	@FXML
	private ComboBox<String> year;

	@FXML
	private ComboBox<String> type;

	@FXML
	private TextField AnswerD;

	@FXML
	private TextField AnswerE;

	@FXML
	private TextField AnswerB;

	@FXML
	private TextField qNo;

	@FXML
	private TextField AnswerC;

	@FXML
	private ComboBox<String> month;

	@FXML
	private TextField AnswerA;

	@FXML
	private ComboBox<String> etype;

	@FXML
	private TextArea Question;

	@FXML
	private ImageView QImage;

	@FXML
	private TextField CorrectAnswer;

	@FXML
	private TextField AnswerH;

	@FXML
	private TextField AnswerI;

	@FXML
	private TextField AnswerF;

	@FXML
	private TextField AnswerG;
	private File questionImage;
	public String section;
	int tid = -1;
	int ttid = -1;
	private int qNumber;

	@FXML
	void imagebrowse(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");

		// Set extension filter
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
		fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

		// Show open file dialog
		questionImage = fileChooser.showOpenDialog(null);
		if (questionImage != null) {
			// System.out.println(selectedFile.getAbsolutePath());
			Image image = new Image(questionImage.getAbsolutePath());
			QImage.setImage(image);
		}
	}

	@FXML
	void btnQCancle(ActionEvent event) {
		questionImage = null;
		QImage.setImage(null);
	}

	@FXML
	void btnquestionAdd(ActionEvent event) {
		try {
			if (validateAnswers()) {
				addData();
				qNo.setText(null);
				Question.setText(null);
				AnswerA.setText(null);
				AnswerB.setText(null);
				AnswerC.setText(null);
				AnswerD.setText(null);
				AnswerE.setText(null);
				AnswerF.setText(null);
				AnswerG.setText(null);
				AnswerH.setText(null);
				AnswerI.setText(null);
				CorrectAnswer.setText(null);
				questionImage = null;
				method();
			}
		} catch (Exception e) {
			e.printStackTrace();
			showAlert("Error", "Failed to add question", "An error occurred while adding the question.");
		}
	}

//	valadition of all method
	private boolean validateAnswers() throws ClassNotFoundException, SQLException {
		if (!validateQuestionNumber())
			return false;
		if (!validateDuplicates())
			return false;
		if (!duplicateCheckQuestionNumber())
			return false;

		// Must be called BEFORE validateSelections()
		if (!validatePresence())
			return false;

		// Now answerText is filled — safe to check
		if (!validateSelections())
			return false;

		System.out.println("No duplicate found. Proceeding with insert...");
		return true;
	}

	private boolean validatePresence() {
		answerText.clear(); // Clear previous values if reused

		TextField[] answers = { AnswerA, AnswerB, AnswerC, AnswerD, AnswerE, AnswerF, AnswerG, AnswerH, AnswerI };
		char label = 'A';

		for (TextField tf : answers) {
			if (isNullOrEmpty(tf))
				return showWarning("Answer (" + label + ")");
			answerText.add(tf.getText().trim());
			label++;
		}

		if (isNullOrEmpty(CorrectAnswer))
			return showWarning("Correct Answer");

		if (isNullOrEmpty1(questionImage, Question))
			return showWarning("Question");

		return true;
	}

	private boolean isNullOrEmpty(TextField answerText) {
		return (answerText == null || answerText.getText().trim().isEmpty());
	}

	private boolean isNullOrEmpty1(File image, TextArea answerText) {
		return image == null && (answerText == null || answerText.getText().trim().isEmpty());
	}

	private boolean showWarning(String fieldName) {
		warningInfo("Error", fieldName + " not found", "Please add your " + fieldName + " as an Image or Text!");
		return false;
	}

	@FXML
	private List<String> answerText = new ArrayList<>();

	private boolean validateSelections() {
		if (year.getValue() == null || month.getValue() == null || chapter.getValue() == null || type.getValue() == null
				|| etype.getValue() == null) {
			warningInfo("Error", "Missing Selection", "Please select values for Year, Month, Chapter, and Type.");
			return false;
		}

		String correct = CorrectAnswer != null ? CorrectAnswer.getText().trim() : "";

		boolean match = answerText.stream().anyMatch(ans -> ans.equalsIgnoreCase(correct));
		System.out.println("Answer List: " + answerText);
		System.out.println("Correct Answer: " + correct);

		if (!match) {
			warningInfo("Error", "Answer Text Mismatch", "Correct answer text not found in your answer options.");
			return false;
		}

		return true;
	}

	private boolean validateQuestionNumber() {
		String text = qNo.getText().trim(); // Trim to remove spaces

		if (text.isEmpty()) { // Check if empty first
			warningInfo("Error", "Missing Addition", "Please add values for question number!");
			return false;
		}

		try {
			qNumber = Integer.parseInt(text); // Convert only if it's not empty

			if (section == "AM" && qNumber > 60) {
				warningInfo("Error", "Invalid Input",
						"Question Number is over (60). Qusetion type is FE->morning(AM) section!!");
				return false;
			}
			return true; // Valid number
		} catch (NumberFormatException e) {
			warningInfo("Error", "Invalid Input", "Please enter a valid number for the question.");
			return false;
		}
	}

	private boolean validateDuplicates() {
		if (hasDuplicateTexts())
			return false;
		return true;
	}

	private boolean hasDuplicateTexts() {
		TextField[] answers = { AnswerA, AnswerB, AnswerC, AnswerD, AnswerE, AnswerF, AnswerG, AnswerH, AnswerI };
		String[] labels = { "A", "B", "C", "D", "E", "F", "G", "H", "I" };
		StringBuilder duplicates = new StringBuilder();

		int i = 0;
		for (TextField answer1 : answers) {
			int j = i + 1;
			for (int k = j; k < answers.length; k++) {
				TextField answer2 = answers[k];
				if (answer1.getText().trim().equalsIgnoreCase(answer2.getText().trim())) {
					duplicates.append("Duplicate between (").append(labels[i]).append(") and (").append(labels[k])
							.append(")\n");
				}
			}
			i++;
		}

		if (duplicates.length() > 0) {
			warningInfo("Error", "Answer Text Duplicate",
					"Please check the following duplicates:\n" + duplicates.toString());
			return true;
		}

		return false;
	}

	private List<String> questionNumberList = new ArrayList<>(); // Ensure it's initialized

	public void getQNO() throws SQLException, ClassNotFoundException {
		// Step 1: Get ym_id from the yearmonth table
		int ym_id = getYMID();
		if (ym_id == -1) {
			System.out.println("No matching ym_id found for the given year and month.");
			return;
		}

		// Step 2: Query the question table using ym_id
		String query = "SELECT q_no FROM question WHERE ym_id = ? and esection_id=?";

		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		try (PreparedStatement pst = con.prepareStatement(query)) {
			pst.setInt(1, ym_id);
			pst.setInt(2,getExamSectionID());

			try (ResultSet rs = pst.executeQuery()) {
				questionNumberList.clear(); // Clear previous results before adding new ones
				while (rs.next()) {
					questionNumberList.add(rs.getString("q_no"));
				}
			}
		} catch (SQLException e) {
			System.err.println("SQL Error in getQNO() for question table : " + e.getMessage());
			e.printStackTrace(); // Print full error stack trace
		}

		// Print the extracted question numbers
		System.out.println("Question Number List for question table: " + questionNumberList);
	}

//public void getQNO() throws SQLException, ClassNotFoundException {
//		
//		System.out.println(section+" this is for question table.");
//		
//		int ym_id = getYMID();
//		if (ym_id == -1) {
//			System.out.println("No matching ym_id found for the given year and month.");
//			return;
//		}
//
//		DatabaseConnection db = new DatabaseConnection();
//		Connection con = db.databaseConnetion();
//		
//		if (etype.getValue().equals("PM")) {
////			section="PM";
//			String query = "SELECT q_no FROM question WHERE ym_id = ? and esection_id=?";
//		try (PreparedStatement pst = con.prepareStatement(query)) {
//			pst.setInt(1, ym_id);
//			pst.setInt(2, getExamSectionID());
//
//			try (ResultSet rs = pst.executeQuery()) {
//				questionNumberList.clear(); // Clear previous results before adding new ones
//				while (rs.next()) {
//					questionNumberList.add(rs.getString("q_no"));
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace(); // Print full error stack trace
//		}
//		System.out.println("Question Number List for question table: " + questionNumberList);
//		}
//	}

	private boolean duplicateCheckQuestionNumber() throws SQLException, ClassNotFoundException {
		getQNO();

		// Ensure that the input question number is valid (non-null and non-empty)
		String questionNumber = qNo.getText();
		if (questionNumber == null || questionNumber.trim().isEmpty()) {
			warningInfo("Error", "Invalid Question Number", "The question number cannot be empty.");
			return false; // Invalid input case
		}

		// Check for duplicates in the list
		if (questionNumberList.contains(questionNumber)) {
			warningInfo("Error", "Duplicate Question Number",
					"A question number for this year and month already exists.");
			return false;
		}

		return true; // No duplicate found
	}
//	-------------------------ID get start ------------------------------

	private int getExamSectionID() throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		try (PreparedStatement pst = con
				.prepareStatement("SELECT DISTINCT esection_id FROM examsection WHERE section_name=?")) {
			pst.setString(1, section);
			ResultSet rs = pst.executeQuery();
			return rs.next() ? rs.getInt(1) : 0;
		}
	}

	private int getYMID() throws SQLException, ClassNotFoundException {
		// Validate year and month before querying
		if (year.getValue() == null || month.getValue() == null) {
			throw new IllegalArgumentException("Year or Month cannot be null.");
		}

		int yId = QuesManageController.getYID(year.getValue());
		int mId = QuesManageController.getMID(month.getValue());
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		String query = "SELECT ym_id FROM yearmonth WHERE y_id=? AND m_id=?";
		try (PreparedStatement pst = con.prepareStatement(query)) {
			pst.setInt(1, yId);
			pst.setInt(2, mId);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1); // Return found ym_id
				} else {
					return -1; // Return -1 to indicate no match found
				}
			}
		}

	}

	private int getYID() throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		try (PreparedStatement pst = con.prepareStatement("SELECT y_id FROM yearchoice WHERE year=?")) {

			pst.setString(1, year.getValue());
			ResultSet rs = pst.executeQuery();
			return rs.next() ? rs.getInt(1) : 0;
		}
	}

	private int getMID() throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		try (PreparedStatement pst = con.prepareStatement("SELECT m_id FROM monthchoice WHERE month=?")) {

			pst.setString(1, month.getValue());
			ResultSet rs = pst.executeQuery();
			return rs.next() ? rs.getInt(1) : 0;
		}
	}

	private int getCID() throws SQLException, ClassNotFoundException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		try (PreparedStatement pst = con.prepareStatement("SELECT c_id FROM chapter WHERE chapter_name=?")) {

			pst.setString(1, chapter.getValue());
			ResultSet rs = pst.executeQuery();
			return rs.next() ? rs.getInt(1) : 0;
		}
	}

	private int getTID() throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		try (PreparedStatement pst = con.prepareStatement("SELECT DISTINCT t_id FROM qtype WHERE category=?")) {

			pst.setString(1, type.getValue());
			ResultSet rs = pst.executeQuery();
			return rs.next() ? rs.getInt(1) : 0;
		}
	}

	private int getETID() throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		try (PreparedStatement pst = con.prepareStatement("SELECT DISTINCT exam_id FROM examtype WHERE exam_name=?")) {

			pst.setString(1, etype.getValue());
			ResultSet rs = pst.executeQuery();
			return rs.next() ? rs.getInt(1) : 0;
		}
	}

//	-------------------------ID get end ------------------------------	
//	-------------------------validation and addition end -------------------------
	// typecombobox and relate chaptercombobox

	public void setupComboBoxes() throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		List<String> examtypeList = new ArrayList<>();
		List<String> typeList = new ArrayList<>();
		List<String> chapterList = new ArrayList<>();

		// Step 1: Load exam types into etype ComboBox
		try (PreparedStatement pst = con.prepareStatement("SELECT DISTINCT exam_name FROM examtype");
				ResultSet rs = pst.executeQuery()) {
			while (rs.next()) {
				String value = normalize(rs.getString("exam_name"));
		        examtypeList.add(value);			}
		}
		etype.getItems().setAll(examtypeList);

		// Step 2: When exam type is selected → load related types
		etype.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null)
				return;

			if ("FE".equals(newValue)) {
				// Create pane with AM/PM buttons
				AnchorPane pane = new AnchorPane();
				Button bt1 = new Button("AM");
				Button bt2 = new Button("PM");
				bt1.setLayoutX(30);
				bt1.setLayoutY(10);
				bt2.setLayoutX(70);
				bt2.setLayoutY(10);

				pane.getChildren().addAll(bt1, bt2);
				Scene sc = new Scene(pane, 150, 50);

				// Create a new Stage for the "alarm"
				Stage popupStage = new Stage();
				popupStage.setTitle("Select Session");
				// Set as a modal dialog (blocks interaction with main window)
				popupStage.initModality(Modality.APPLICATION_MODAL);
				// Set the owner to the current window
				popupStage.initOwner(etype.getScene().getWindow());
				popupStage.initStyle(StageStyle.UNDECORATED);

				// Set the scene and show
				popupStage.setScene(sc);
				// Handle AM click
				bt1.setOnAction(event -> {
					section = "AM";
					System.out.println("AM selected");
					popupStage.close(); // close dialog
					// You can handle AM logic here
				});

				// Handle PM click
				bt2.setOnAction(event -> {
					section = "PM";
					popupStage.close(); // close dialog
					try {
						QuestionFEForm form = new QuestionFEForm();
						Stage stage = form.questionFEForm();
						stage.show();

						// Later:
						QuestionForm.questionsForm.close();
//						form.close(); // closes the form

					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				// Get screen coordinates of the ComboBox
				Bounds boundsInScreen = etype.localToScreen(etype.getBoundsInLocal());
				double popupWidth = 150; // same as the scene width
				double popupHeight = 50; // same as the scene height

				// Position to the left of ComboBox
				popupStage.setX(boundsInScreen.getMinX() - popupWidth + 100); // 10px spacing
				popupStage.setY(boundsInScreen.getMinY() - popupHeight + 90); // align vertically

				popupStage.showAndWait(); // use show() if you don't want it to block
			}else{
				section = "IP";
//				QuestionForm form = new QuestionForm();
//				Stage stage = null;
//				try {
//					stage = form.questionForm();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				stage.show();
//
//				// Later:
//				QuestionFEForm.questionFeForm.close();
			}
			try {
				int tid = 0;
				try (PreparedStatement pst = con.prepareStatement("SELECT exam_id FROM examtype WHERE exam_name = ?")) {
					pst.setString(1, newValue);
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							tid = rs.getInt("exam_id");
						}
					}
				}

				// Get type IDs
				List<String> typeIdList = new ArrayList<>();
				try (PreparedStatement pst = con.prepareStatement("SELECT t_id FROM chaptye WHERE exam_id = ?")) {
					pst.setInt(1, tid);
					try (ResultSet rs = pst.executeQuery()) {
						while (rs.next()) {
							typeIdList.add(rs.getString("t_id"));
						}
					}
				}

				// Get type names
				typeList.clear();
				for (String t_id : typeIdList) {
				    try (PreparedStatement pst = con.prepareStatement(
				            "SELECT DISTINCT LOWER(TRIM(category)) AS normalized_category FROM qtype WHERE t_id = ?")) {
				        pst.setString(1, t_id);
				        try (ResultSet rs = pst.executeQuery()) {
				            while (rs.next()) {
				            	String value = rs.getString("normalized_category");
				            	value = value.substring(0, 1).toUpperCase() + value.substring(1);
				                if (!typeList.contains(value)) {
				                    typeList.add(value);
				                }
				            }
				        }
				    }
				}

				type.getItems().setAll(typeList);

				// Set listener for type ComboBox
				type.getSelectionModel().selectedItemProperty().addListener((typeObs, typeOld, typeNew) -> {
					if (typeNew == null)
						return;

					int ttid = 0;

					try (PreparedStatement pst = con.prepareStatement("SELECT t_id FROM qtype WHERE category = ?")) {
						pst.setString(1, typeNew);
						try (ResultSet rs = pst.executeQuery()) {
							if (rs.next()) {
								ttid = rs.getInt("t_id");
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}

					// Get chapter IDs
					List<String> chapterIdList = new ArrayList<>();
					try (PreparedStatement pst = con.prepareStatement("SELECT c_id FROM chaptye WHERE t_id = ?")) {
						pst.setInt(1, ttid);
						try (ResultSet rs = pst.executeQuery()) {
							while (rs.next()) {
								chapterIdList.add(rs.getString("c_id"));
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}

					// Get chapter names
					chapterList.clear();
					for (String c_id : chapterIdList) {
					    try (PreparedStatement pst = con.prepareStatement(
					            "SELECT DISTINCT LOWER(TRIM(chapter_name)) AS normalized_chapter FROM chapter WHERE c_id = ?")) {
					        pst.setString(1, c_id);
					        try (ResultSet rs = pst.executeQuery()) {
					            while (rs.next()) {
					                String value = rs.getString("normalized_chapter");
					                value = value.substring(0, 1).toUpperCase() + value.substring(1); // Capitalize first letter
					                if (!chapterList.contains(value)) {
					                    chapterList.add(value);
					                }
					            }
					        }
					    } catch (SQLException e) {
					        e.printStackTrace();
					    }
					}
					chapter.getItems().setAll(chapterList);
				});

			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

//----------------------------------------------------------------------------------------------
	// yearcombobox
	public ComboBox<String> yearComboBox() throws SQLException {
		List<String> typeList = new ArrayList<>();
		List<String> monthIdList = new ArrayList<>();
		List<String> monthList = new ArrayList<>();

		// Fetch years for the ComboBox
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		try (PreparedStatement pst = con.prepareStatement("SELECT DISTINCT year FROM yearchoice");
				ResultSet rs = pst.executeQuery()) {
			while (rs.next()) {
				String value = normalize(rs.getString("year"));
				typeList.add(value);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		year.getItems().setAll(typeList);

		// Adding a ChangeListener to track selection changes
		year.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				System.out.println("Selected: " + newValue);

				// Fetch the corresponding ID for the selected year
				int id = 0;
				try (PreparedStatement pst = con.prepareStatement("SELECT y_id FROM yearchoice WHERE year = ?")) {
					pst.setString(1, newValue);
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							id = rs.getInt("y_id"); // Update the id variable
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				// Fetch month IDs based on selected year ID
				monthIdList.clear(); // Clear previous month IDs
				try (PreparedStatement pst = con.prepareStatement("SELECT m_id FROM yearmonth WHERE y_id = ?")) {
					pst.setInt(1, id); // Use parameterized query to prevent SQL injection

					try (ResultSet rs = pst.executeQuery()) {
						while (rs.next()) {
							monthIdList.add(rs.getString("m_id")); // Adding month ID
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				// Fetch months based on month IDs
				monthList.clear(); // Clear previous month list before adding new months
				for (String m_id : monthIdList) {
					try (PreparedStatement pst = con.prepareStatement("SELECT DISTINCT month FROM monthchoice WHERE m_id = ?")) {
						pst.setString(1, m_id); // Use parameterized query for m_id

						try (ResultSet rs = pst.executeQuery()) {
							if (rs.next()) {
								String value = normalize(rs.getString("month"));
								monthList.add(value);
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				// Update ComboBox items with the new month list
				Platform.runLater(() -> {
//				    ComboBox<String> month = new ComboBox<>();
					month.getItems().setAll(monthList); // Set months to the ComboBox
				});
			}
		});

		return year; // Return the ComboBox so it can be added to the UI
	}
	
	private String normalize(String input) {
	    return input == null ? "" :
	        input.trim()
	             .replaceAll("\\s+", " "); // normalize multiple spaces
	}

	private void addData() throws SQLException, IOException, ClassNotFoundException {
		System.out.println("yearmaonth id is " + getYMID());
		System.out.println("year id is : " + getYID());
		System.out.println("month id is : " + getMID());
		System.out.println("chapter id is " + getCID());
		System.out.println("type id is " + getTID());
		DatabaseConnection db = new DatabaseConnection();
		try (Connection con = db.getConnetion()) {
			con.setAutoCommit(false);

			String insertSQL = "INSERT INTO questionnumber (q_no, y_id, m_id) VALUES (?, ?, ?)";

			try (PreparedStatement pst = con.prepareStatement(insertSQL)) {

				pst.setInt(1, qNumber);
				pst.setInt(2, getYID());
				pst.setInt(3, getMID());

				int rowsInserted = pst.executeUpdate();
				if (rowsInserted > 0) {
					System.out.println(
							"Inserted successfully: q_no=" + qNo + ", y_id=" + getYID() + ", m_id=" + getMID());
				} else {
					System.out.println("Insert failed.");
				}

			} catch (SQLException e) {
				System.err.println("SQL Error in insertQuestionNumber(): " + e.getMessage());
			}

			String imageSQL = "INSERT INTO questionimage(q_image) VALUES (?)";
			try (PreparedStatement pst1 = con.prepareStatement(imageSQL, Statement.RETURN_GENERATED_KEYS)) {
				pst1.setBinaryStream(1, questionImage != null ? new FileInputStream(questionImage) : null);

				pst1.executeUpdate();
				ResultSet rs1 = pst1.getGeneratedKeys();
				int imgId = rs1.next() ? rs1.getInt(1) : 0;

				String questionSQL = "INSERT INTO question(q_no, description, answer_a, answer_b, answer_c, answer_d,answer_e,answer_f,answer_g,answer_h,"
						+ "answer_i,`answer`, img_id, c_id, t_id,exam_id,esection_id, ym_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				try (PreparedStatement pst2 = con.prepareStatement(questionSQL)) {
					pst2.setInt(1, qNumber);
					pst2.setString(2, Question.getText());
					pst2.setString(3, AnswerA.getText());
					pst2.setString(4, AnswerB.getText());
					pst2.setString(5, AnswerC.getText());
					pst2.setString(6, AnswerD.getText());
					pst2.setString(7, AnswerE.getText());
					pst2.setString(8, AnswerF.getText());
					pst2.setString(9, AnswerG.getText());
					pst2.setString(10, AnswerH.getText());
					pst2.setString(11, AnswerI.getText());
					pst2.setString(12, CorrectAnswer.getText());
					pst2.setInt(13, imgId);
					pst2.setInt(14, getCID());
					pst2.setInt(15, getTID());
					pst2.setInt(16, getETID());
					pst2.setInt(17, getExamSectionID());
					pst2.setInt(18, getYMID());
					pst2.executeUpdate();
				}
			}

			con.commit();
			showAlert("Success", "Question Added", "The question has been successfully added.");
		}
	}

	public void method() {
		try {
			yearComboBox();
//			typeComboBox();
			setupComboBoxes();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showAlert(String title, String header, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}

	private void warningInfo(String title, String header, String content) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		method();

	}

}
