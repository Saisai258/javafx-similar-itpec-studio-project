package application.teacher.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.main.DatabaseConnection;
import application.teacher.Question;
import application.teacher.form.BeforeUpdateQuestionFormCopy;
import application.teacher.form.QuestionLookFeForm;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class CheckQuestionFeController implements Initializable {
	public static Question check_Question;
	private CheckBox selectedChapterCheckBox;
	private CheckBox selectedYearMonthCheckBox;
	ObservableList<Question> questionList = FXCollections.observableArrayList();

	@FXML
	private Button btnsearch;

	@FXML
	private ScrollPane chScrollPane;

	@FXML
	private TextField qNo;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private TableColumn<Question, String> examType;

	@FXML
	private TableView<Question> questionTable;

	@FXML
	private TableColumn<Question, String> chapterColumn;
	@FXML
	private TableColumn<Question, Integer> questionNumberColumn;
	@FXML
	private TableColumn<Question, String> yearMonthColumn;

	@FXML
	private TextField txtSearch;

	@FXML
	void handleMouseAction(MouseEvent event) {
		Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();

		if (selectedQuestion != null) {
			// Set the selected question number to qNo TextField
			qNo.setText(String.valueOf(selectedQuestion.getQNo()));

			// Select corresponding Year-Month CheckBox
			selectedYearMonthCheckBox = selectCheckBoxById((VBox) scrollPane.getContent(), selectedYearMonthCheckBox,
					selectedQuestion.getYm_id());

			// Select corresponding Chapter CheckBox
			selectedChapterCheckBox = selectCheckBoxById((VBox) chScrollPane.getContent(), selectedChapterCheckBox,
					selectedQuestion.getC_id());
		}
	}

	private CheckBox selectCheckBoxById(VBox vbox, CheckBox previouslySelected, int targetId) {
		if (vbox == null)
			return null; // Prevent NullPointerException

		for (javafx.scene.Node node : vbox.getChildren()) {
			if (node instanceof CheckBox checkBox && checkBox.getId().equals(String.valueOf(targetId))) {
				if (previouslySelected != null) {
					previouslySelected.setSelected(false);
				}
				checkBox.setSelected(true);
				return checkBox;
			}
		}
		return null; // Return null if no matching checkbox is found
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (qNo == null || scrollPane == null || chScrollPane == null) {
			showAlert(AlertType.ERROR, "Initialization Error", "UI Component Missing",
					"Some UI components were not loaded properly. Please restart the application.");
			return;
		}

		questionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("qNo")); // Ensure this matches the getter
		chapterColumn.setCellValueFactory(cellData -> {
			Question question = cellData.getValue();
			int chapterId = question.getC_id();
			String chapterName = getChapterName(chapterId);
			return new SimpleStringProperty(chapterName);
		});

		yearMonthColumn.setCellValueFactory(cellData -> {
			Question question = cellData.getValue();
			int ymId = question.getYm_id();
			String yearMonth = getYearMonthString(ymId);
			return new SimpleStringProperty(yearMonth);
		});
		examType.setCellValueFactory(cellData -> {
	        cellData.getValue();
	        String examTypeText = "FE( AM )";
	        return new SimpleStringProperty(examTypeText);
	    });


		try {
			populateYearMonthCheckBox();
			chapterCheckBox();
			loadTableData();
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Database Error", "Data Load Failed",
					"There was an issue retrieving data from the database.");
		}
	}

	private String getChapterName(int chapterId) {
		String sql = "SELECT chapter_name FROM chapter WHERE c_id = ?";

		try (Connection con = new DatabaseConnection().getConnetion();
				PreparedStatement pst = con.prepareStatement(sql)) {

			pst.setInt(1, chapterId);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				return rs.getString("chapter_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Unknown Chapter";
	}

	private String getYearMonthString(int ymId) {
		String sql = "SELECT yc.year, mc.month FROM yearmonth ym " + "INNER JOIN yearchoice yc ON ym.y_id = yc.y_id "
				+ "INNER JOIN monthchoice mc ON ym.m_id = mc.m_id " + "WHERE ym.ym_id = ?";

		try (Connection con = new DatabaseConnection().getConnetion();
				PreparedStatement pst = con.prepareStatement(sql)) {

			pst.setInt(1, ymId);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				String year = rs.getString("year");
				String month = rs.getString("month");
				return year + " " + month;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Unknown Exam";
	}
	
	private void loadTableData() throws SQLException {

		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		String sql = "SELECT * FROM ipquestion WHERE esection_id = 2";
		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();

		while (rs.next()) {
			int qid = rs.getInt("Qip_id");
			int qNo = rs.getInt("q_no");
			String qDesc = rs.getString("description");
			String ansA = rs.getString("answer_a");
			String ansB = rs.getString("answer_b");
			String ansC = rs.getString("answer_c");
			String ansD = rs.getString("answer_d");
			String qAns = rs.getString("answer");
			int ym_id = rs.getInt("ym_id");
			int c_id = rs.getInt("c_id");
			int t_id = rs.getInt("t_id");
			int img_id = rs.getInt("img_id");
			int exam_id = rs.getInt("exam_id");
			int esection_id = rs.getInt("esection_id");

			PreparedStatement pst1 = con.prepareStatement(
					"SELECT q_image, ans_img_a, ans_img_b, ans_img_c, ans_img_d, ans_cor_img FROM questionimage WHERE img_id = ?");
			pst1.setInt(1, img_id);
			ResultSet rs1 = pst1.executeQuery();

			if (rs1.next()) {
				Question question = new Question(qid, qNo, qDesc, ansA, ansB, ansC, ansD, qAns,
						ym_id, img_id, c_id, t_id,
						rs1.getBytes("q_image"), exam_id, esection_id,
						rs1.getBytes("ans_img_a"), rs1.getBytes("ans_img_b"),
						rs1.getBytes("ans_img_c"), rs1.getBytes("ans_img_d"),
						rs1.getBytes("ans_cor_img"));
				questionList.add(question);

			}
			}

		questionTable.setItems(questionList);
	}
	public void populateYearMonthCheckBox() throws SQLException {
		String sql = "SELECT ym.ym_id, yc.year, mc.month FROM yearmonth ym INNER JOIN yearchoice yc ON ym.y_id = yc.y_id INNER JOIN monthchoice mc ON ym.m_id = mc.m_id";
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();

		VBox vbox = new VBox(10);
		Label titleLabel = new Label("Select Quiz Set");
		titleLabel.setStyle(
				"-fx-font-size:  20px; -fx-font-style:  italic; -fx-font-family:System; -fx-padding:7 0 3 10;");
		vbox.getChildren().add(titleLabel);

		while (rs.next()) {
			int ymId = rs.getInt("ym_id");
			String year = rs.getString("year");
			String month = rs.getString("month");
			CheckBox checkBox = new CheckBox(year + " " + month + " Exam");
			checkBox.setStyle("-fx-padding:0 10;");
			checkBox.setCursor(Cursor.HAND);
			checkBox.setId(String.valueOf(ymId));
			checkBox.setOnAction(event -> {
				if (selectedYearMonthCheckBox != null) {
					selectedYearMonthCheckBox.setSelected(false);
				}
				selectedYearMonthCheckBox = checkBox;
			});
			vbox.getChildren().add(checkBox);
		}
		scrollPane.setContent(vbox);
		scrollPane.setFitToWidth(true);
	}

	public void chapterCheckBox() throws SQLException {
		String sql = "SELECT * FROM chapter";
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();

		VBox vbox = new VBox(10);
		Label titleLabel = new Label("Select Category");
		titleLabel.setStyle(
				"-fx-font-size:  20px; -fx-font-style:  italic; -fx-font-family:System; -fx-padding:7 0 3 10;");
		vbox.getChildren().add(titleLabel);

		while (rs.next()) {
			String chapter = rs.getString("chapter_name");
			int chapterId = rs.getInt("c_id");
			CheckBox checkBox = new CheckBox(chapter);
			checkBox.setId(String.valueOf(chapterId));
			checkBox.setStyle("-fx-padding:0 10;");
			checkBox.setCursor(Cursor.HAND);
			checkBox.setOnAction(event -> {
				if (selectedChapterCheckBox != null) {
					selectedChapterCheckBox.setSelected(false);
				}
				selectedChapterCheckBox = checkBox;
			});
			vbox.getChildren().add(checkBox);
		}
		chScrollPane.setContent(vbox);
		chScrollPane.setFitToWidth(true);
	}

	private void showAlert(AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	@FXML
	void btnsearchAction(ActionEvent event) {
		String searchText = txtSearch.getText().trim();

		// If search is empty, reset the table to show all questions
		if (searchText.isEmpty()) {
			questionTable.setItems(questionList);
			showAlert(AlertType.WARNING, "Search Error", "No Input", "Please enter a question number to search.");
			return;
		}

		try {
			int searchQNo = Integer.parseInt(searchText);
			ObservableList<Question> filteredList = FXCollections.observableArrayList();

			// Always search from the full original list
			for (Question q : questionList) {
				if (q.getQNo() == searchQNo) {
					filteredList.add(q);
				}
			}

			// Update the table with filtered results
			questionTable.setItems(filteredList);

			// Show alert if no results are found
			if (filteredList.isEmpty()) {
				showAlert(AlertType.INFORMATION, "No Results", "No Matching Questions",
						"No question found for QNo: " + searchQNo);
			}
		} catch (NumberFormatException e) {
			showAlert(AlertType.ERROR, "Invalid Input", "Search Must Be a Number",
					"Please enter a valid numeric Question Number.");
		}
	}

	@FXML
	void updateQuestionLinkAction(ActionEvent event) throws IOException {
		new BeforeUpdateQuestionFormCopy().toggleForm();
	}

	@FXML
	void forQuesCheck(ActionEvent event) throws IOException, SQLException {
		String qNoText = qNo.getText().trim();

		// Check if qNo is empty
		if (qNoText.isEmpty()) {
			showAlert(AlertType.WARNING, "Input Required", "Missing Question Number",
					"Please enter a valid Question Number.");
			return;
		}

		// Check if qNo is a number between 1 and 100
		try {
			int qNumber = Integer.parseInt(qNoText);
			if (qNumber < 1 || qNumber > 100) {
				showAlert(AlertType.WARNING, "Invalid Input", "Out of Range",
						"Question Number must be between 1 and 100.");
				return;
			}
		} catch (NumberFormatException e) {
			showAlert(AlertType.ERROR, "Invalid Input", "Not a Number",
					"Please enter a valid numeric Question Number (1-100).");
			return;
		}

		// Check if a category (chapter) is selected
		if (selectedChapterCheckBox == null) {
			showAlert(AlertType.WARNING, "Selection Required", "No Category Selected",
					"Please select a category before proceeding.");
			return;
		}

		// Check if a quiz set (year-month) is selected
		if (selectedYearMonthCheckBox == null) {
			showAlert(AlertType.WARNING, "Selection Required", "No Quiz Set Selected",
					"Please select a quiz set before proceeding.");
			return;
		}

		// Get the selected YearMonth text and Chapter text
		String selectedYearMonthText = selectedYearMonthCheckBox.getText();
		String selectedChapterText = selectedChapterCheckBox.getText();

		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("SELECT * FROM ipquestion WHERE q_no=? AND c_id = ? AND ym_id = ? AND esection_id = 2");
		pst.setInt(1, Integer.parseInt(qNoText));
		pst.setInt(2, Integer.parseInt(selectedChapterCheckBox.getId()));
		pst.setInt(3, Integer.parseInt(selectedYearMonthCheckBox.getId()));

		ResultSet rs = pst.executeQuery();

		if (!rs.next()) {
			showAlert(AlertType.INFORMATION, "No Question Found", "Question Not Found",
					"No question exists with the given details:\n" + "Year-Month: " + selectedYearMonthText + "\n"
							+ "Category: " + selectedChapterText);
			return;
		}

		do {
			int qid = rs.getInt("Qip_id");
			int qNo = rs.getInt("q_no");
			String qDesc = rs.getString("description");
			String ansA = rs.getString("answer_a");
			String ansB = rs.getString("answer_b");
			String ansC = rs.getString("answer_c");
			String ansD = rs.getString("answer_d");
			String qAns = rs.getString("answer");
			int ym_id = rs.getInt("ym_id");
			int c_id = rs.getInt("c_id");
			int t_id = rs.getInt("t_id");
			int img_id = rs.getInt("img_id");
			int exam_id = rs.getInt("exam_id");
			int esection_id = rs.getInt("esection_id");
			String selectImageSQL = "SELECT q_image, ans_img_a, ans_img_b, ans_img_c, ans_img_d, ans_cor_img FROM questionimage WHERE img_id = ?";
			PreparedStatement pst1 = con.prepareStatement(selectImageSQL);
			pst1.setInt(1, img_id);
			ResultSet rs1 = pst1.executeQuery();

			if (rs1.next()) {
				byte[] qImg = rs1.getBytes("q_image");
				byte[] ansAImg = rs1.getBytes("ans_img_a");
				byte[] ansBImg = rs1.getBytes("ans_img_b");
				byte[] ansCImg = rs1.getBytes("ans_img_c");
				byte[] ansDImg = rs1.getBytes("ans_img_d");
				byte[] qAnsImg = rs1.getBytes("ans_cor_img");

				check_Question = new Question(qid, qNo, qDesc, ansA, ansB, ansC, ansD, qAns, ym_id, img_id, c_id, t_id,
						qImg,exam_id,esection_id, ansAImg, ansBImg, ansCImg, ansDImg, qAnsImg);
			}
		} while (rs.next());

		QuestionLookFeForm upc = new QuestionLookFeForm();
		upc.questionLookForm().show();
	}
}
