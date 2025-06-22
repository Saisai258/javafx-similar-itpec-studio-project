package application.student.controller;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import application.main.DatabaseConnection;
import application.teacher.Question;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class FeAmChController implements Initializable {

	public static Map<Integer, String> userAnswerSelections = new HashMap<>(); // Store question index -> selected
																				// answer
	private Set<Integer> answeredQuestions = new HashSet<>();
	String ExamName = ExamController.ExamName;
	public static int examTotalMark;
	public static int yearMonthCBO;
	public static int[] answerUser;
	public static int k = 0;
	public static List<Question> questionList;
	List<Integer> chapterCheckId;
	List<Integer> yearmonthCheckId;
	public static List<Question> limitedQuestions;
	private List<CheckBox> chapterCheckBoxes = new ArrayList<>();
	private List<CheckBox> yearMonthCheckBoxes = new ArrayList<>();

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private Spinner<Integer> qSpinner;

	@FXML
	private CheckBox chkRandom;

	@FXML
	private ScrollPane chScrollPane;

	public void initialize(URL location, ResourceBundle resources) {

		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 100, 10, 5);
		valueFactory.setValue(10);
		qSpinner.setValueFactory(valueFactory);
		try {
			populateYearMonthCheckBox();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			chapterCheckBox();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static Stage stage;

	@FXML
	void testLinkAction(ActionEvent event) throws SQLException {
		// Check selections and show alerts if needed
		checkSelectionAndShowAlert();

		// If selections are invalid, stop execution
		if (!isSelectionValid()) {
			return;
		}

		questionList = new ArrayList<>();
		chapterCheckId = new ArrayList<>();
		yearmonthCheckId = new ArrayList<>();
		getSelectedYmId();
		getSelectedChapter();

		try {
			stage = new Stage();
			ScrollPane questionScrollPane = showQuestionList();

			Scene scene = new Scene(questionScrollPane, 1366, 700);
			stage.setScene(scene);
			stage.getIcons().add(new Image("downLogo.jpg"));
			stage.setTitle("Quiz Mania Ultimate Challenge");	
			stage.show();
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert("Database Error", "An error occurred while loading the question list. Please try again later.");
		} catch (Exception e) {
			e.printStackTrace();
			showAlert("Error", "An unexpected error occurred. Please try again.");
		}
	}

	public ScrollPane showQuestionList() throws SQLException {
		VBox mainVBox = new VBox();
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(mainVBox);
		mainVBox.setAlignment(Pos.CENTER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setPrefSize(1366, 700);

		mainVBox.setSpacing(10);
		mainVBox.setPrefSize(1366, 700);
		mainVBox.setAlignment(Pos.TOP_CENTER);
		mainVBox.setStyle("-fx-background-color:lightblue;");

		// Create the header
		Label headerLabel = new Label("Practice FE Questions");
		headerLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: navy;");
		headerLabel.setAlignment(Pos.CENTER);

		// Add the header to the top of the main VBox
		mainVBox.getChildren().add(headerLabel);

		VBox vboxQuestion = new VBox();
		vboxQuestion.setSpacing(10);
		vboxQuestion.setPrefWidth(1366);
		vboxQuestion.setAlignment(Pos.TOP_CENTER);

		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		getQueryQuestion();// add question List

		// Check if "Random" check box is selected
		if (chkRandom.isSelected()) {
			// Shuffle the questions randomly
			Collections.shuffle(questionList);
		}

		// Limit questions to the spinner value
		int questionLimit = qSpinner.getValue();
		int totalQuestions = Math.min(questionList.size(), questionLimit);
		limitedQuestions = questionList.subList(0, totalQuestions);

		int rowCount = limitedQuestions.size(); // Fixed to use limitedQuestions
		String[] answerKeyGroup = new String[rowCount];
		answerUser = new int[rowCount];
		int ansNo = 0;

		// Assuming you have a method to get the correct answer for each question
		for (int i = 0; i < limitedQuestions.size(); i++) {
			VBox questionPane = createQuestionPane(limitedQuestions.get(i), answerKeyGroup, ansNo++, i + 1);
			vboxQuestion.getChildren().add(questionPane);
		}

		// Add questions VBox to the main VBox
		mainVBox.getChildren().add(vboxQuestion);

		// Create and style the Cancel button
		Button btnCancel = new Button("Cancel");
		btnCancel.setCursor(Cursor.HAND);
		btnCancel.setStyle(
				"-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px;");

		// Create and style the Submit button
		Button btnSubmit = new Button("Submit");
		btnSubmit.setCursor(Cursor.HAND);
		btnSubmit.setStyle(
				"-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px;");

		// Arrange the buttons horizontally using HBox
		HBox buttonBox = new HBox(20); // Adds spacing between buttons
		buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
		buttonBox.setPadding(new Insets(50));
		buttonBox.getChildren().addAll(btnCancel, btnSubmit);
		mainVBox.getChildren().add(buttonBox);

		// Submit button action
		btnSubmit.setOnAction(e -> {
			String resultSQL = "INSERT INTO examresult (exam_date, question_number, exam_name, exam_type, total_mark, student_id) VALUES (?, ?, ?, ?, ?, ?)";

			try {
				// Step 1: Get today's date
				LocalDate todayDate = LocalDate.now();
				Date sqlDate = Date.valueOf(todayDate);

				// Step 2: Calculate the total marks based on correct answers
				int totalMarks = 0;
				for (int i = 0; i < answerUser.length; i++) {
					if (answerUser[i] == 1) {
						totalMarks++; // Count correct answers
					}
				}
				examTotalMark = totalMarks;

				// Step 3: Get the logged-in student ID
				String studentID = SignInController.login_student.getStudent_id();
//				int studentID = getSID(s_id);

//				if (studentID != 0) {
				// Step 4: Insert the result into the database
				PreparedStatement pst2 = con.prepareStatement(resultSQL);
				pst2.setDate(1, sqlDate); // Exam date
				pst2.setInt(2, totalQuestions); // Total questions
				pst2.setString(3, ExamName); // Exam name (set default "IP")
				pst2.setString(4, "Practice"); // Exam type (set default "Practice")
				pst2.setInt(5, totalMarks); // Total marks
				pst2.setString(6, studentID); // Student ID

				pst2.executeUpdate();

			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			stage.close();
			// Show result view
			Stage currentStage = (Stage) scrollPane.getScene().getWindow();
			FEResultController resultController = new FEResultController();
			resultController.setReviewData(limitedQuestions, answerUser, totalQuestions);

			BorderPane resultView = resultController.getResultView();

			Scene resultScene = new Scene(resultView, 1366, 700);
			currentStage.getIcons().add(new Image("downLogo.jpg"));
			currentStage.setTitle("Quiz Mania Ultimate Challenge");
			currentStage.setScene(resultScene);
			currentStage.show();
		});

		// Cancel button action
		btnCancel.setOnAction(e -> {
			// Close the window or reset the current view
			Stage currentStage = (Stage) scrollPane.getScene().getWindow();
			currentStage.close();
		});

		stage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
			if (!isNowFocused) {
				stage.close();
			}
		});
		return scrollPane;
	}

	public void getQueryQuestion() throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		for (int i = 0; i < chapterCheckId.size(); i++) { // Start at 0 and loop up to size - 1
			for (int j = 0; j < yearmonthCheckId.size(); j++) { // Start at 0 for yearmonthCheckId
				PreparedStatement pst = con
						.prepareStatement("SELECT * FROM ipquestion WHERE c_id = ? AND ym_id = ? AND exam_id = 2");

				pst.setInt(1, chapterCheckId.get(i)); // Valid indexing for chapterCheckId
				pst.setInt(2, yearmonthCheckId.get(j)); // Valid indexing for yearmonthCheckId

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

						Question q = new Question(qid, qNo, qDesc, ansA, ansB, ansC, ansD, qAns, ym_id, img_id, c_id,
								t_id, qImg, exam_id, esection_id, ansAImg, ansBImg, ansCImg, ansDImg, qAnsImg);
						questionList.add(q);
					}
				}
			}
		}

	}

	public VBox createQuestionPane(Question question, String[] answerKeyGroup, int ansNo, int qNo) throws SQLException {
		VBox questionPane = new VBox(10);
		questionPane.setMaxWidth(900);
		questionPane.setPadding(new Insets(20));

		// Question Number, Description, and Image in HBox
		VBox questionBox = new VBox(10);
		questionBox.setAlignment(Pos.CENTER_LEFT);

		Label lblQNo = new Label("Q: " + qNo);
		lblQNo.setWrapText(true);
		lblQNo.setFont(Font.font(18));
		lblQNo.setMinWidth(50);
		lblQNo.setMinHeight(Region.USE_PREF_SIZE);

		Label txtQDesp = new Label(question.getQ_desc());
		txtQDesp.setWrapText(true);
		txtQDesp.setMaxWidth(Double.MAX_VALUE);
		txtQDesp.setMinHeight(Region.USE_PREF_SIZE);
		txtQDesp.setFont(Font.font(16));

		// Optional: Make row auto-resize
		VBox.setVgrow(txtQDesp, Priority.ALWAYS);

		// Store the correct answer text for reference
		answerKeyGroup[ansNo] = question.getQ_ans();

		ImageView questionImage = null;
		int imgId = question.getImg_id();

		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		String imageQuery = "SELECT * FROM questionImage WHERE img_id = ?";
		PreparedStatement imgStmt = con.prepareStatement(imageQuery);
		imgStmt.setInt(1, imgId);
		ResultSet imageRS = imgStmt.executeQuery();

		if (imageRS.next()) {
			InputStream mainImgStream = imageRS.getBinaryStream("q_image");
			if (mainImgStream != null) {
				Image mainImage = new Image(mainImgStream);
				questionImage = new ImageView(mainImage);
				questionImage.setFitHeight(200);
				questionImage.setPreserveRatio(true);
			}
		}

		HBox questionBox1 = new HBox(10);
		questionBox.setAlignment(Pos.CENTER_LEFT);

		questionBox1.getChildren().addAll(lblQNo, txtQDesp);
		questionBox.getChildren().addAll(questionBox1);
		if (questionImage != null) {
			questionBox.getChildren().add(questionImage);
		}

		questionPane.getChildren().add(questionBox);

		// Answer options in HBox
		String[] answers = { "answer_a", "answer_b", "answer_c", "answer_d" };
		String[] imgColumns = { "ans_img_a", "ans_img_b", "ans_img_c", "ans_img_d" };
		ToggleGroup answerGroup = new ToggleGroup();

		String textQuery = "SELECT * FROM ipquestion WHERE img_id = ?";
		PreparedStatement textStmt = con.prepareStatement(textQuery);
		textStmt.setInt(1, imgId);
		ResultSet textRS = textStmt.executeQuery();

		if (textRS.next()) {
			for (int i = 0; i < answers.length; i++) {
				String answerText = textRS.getString(answers[i]);
				InputStream answerImgStream = (imageRS != null) ? imageRS.getBinaryStream(imgColumns[i]) : null;

				if (answerText != null) {
					HBox answerBox = new HBox(10);
					answerBox.setAlignment(Pos.CENTER_LEFT);

					char optionLetter = (char) ('A' + i);
					Label optionLabel = new Label(optionLetter + ".");
					optionLabel.setFont(Font.font(16));
					optionLabel.setWrapText(true);
					optionLabel.setMinWidth(50);
					optionLabel.setMinHeight(Region.USE_PREF_SIZE);

					RadioButton rbAnswer = new RadioButton(answerText);
					rbAnswer.setCursor(Cursor.HAND);
					rbAnswer.setWrapText(true);
					rbAnswer.setToggleGroup(answerGroup);
					rbAnswer.setMaxWidth(500);
					rbAnswer.setMinHeight(Region.USE_PREF_SIZE);
					rbAnswer.setFont(Font.font(16));

					// Inside the rbAnswer.setOnAction() method
					rbAnswer.setOnAction(e -> {
					    String correctAnswer = question.getQ_ans();
					    String normalizedCorrect = normalize(correctAnswer);
					    String normalizedUser = normalize(answerText);

					    // Check if user's selected answer matches correct answer
					    if (normalizedCorrect.equals(normalizedUser)) {
					        answerUser[ansNo] = 1; // Correct
					    } else {
					        answerUser[ansNo] = 0; // Incorrect
					    }

					    // Save user answer and mark question as answered
					    getUserAnswerSelections().put(ansNo, answerText);
					    getAnsweredQuestions().add(ansNo);
					});

					ImageView answerImageView = null;
					if (answerImgStream != null) {
						Image answerImage = new Image(answerImgStream);
						answerImageView = new ImageView(answerImage);
						answerImageView.setFitHeight(150);
						answerImageView.setPreserveRatio(true);
					}

					answerBox.getChildren().addAll(optionLabel, rbAnswer);
					if (answerImageView != null) {
						answerBox.getChildren().add(answerImageView);
					}

					questionPane.getChildren().add(answerBox);
				}
			}
		}
		return questionPane;
	}
	private String normalize(String input) {
	    return input == null ? "" :
	        input.trim()
	             .replaceAll("\\s+", " ")
	             .toLowerCase()
	             .replaceAll("[^a-z0-9]", "");
	}

	private boolean isSelectionValid() {
		boolean ymSelected = false;
		boolean chSelected = false;

		for (CheckBox cb : yearMonthCheckBoxes) {
			if (cb.isSelected()) {
				ymSelected = true;
				break;
			}
		}

		for (CheckBox cb : chapterCheckBoxes) {
			if (cb.isSelected()) {
				chSelected = true;
				break;
			}
		}

		return ymSelected && chSelected;
	}

	public void getSelectedYmId() {
		if (yearMonthCheckBoxes != null) {
			for (CheckBox checkBox : yearMonthCheckBoxes) {
				if (checkBox.isSelected()) {
					Integer checkBoxId = Integer.parseInt(checkBox.getId());
					yearmonthCheckId.add(checkBoxId);

				}
			}
		}

	}

	private void getSelectedChapter() {
		if (chapterCheckBoxes != null) {
			for (CheckBox chCheckBox : chapterCheckBoxes) {
				if (chCheckBox.isSelected()) {
					Integer checkBoxId = Integer.parseInt(chCheckBox.getId());
					chapterCheckId.add(checkBoxId);
				}
			}
		}
	}

	public List<CheckBox> populateYearMonthCheckBox() throws SQLException {
		if (!yearMonthCheckBoxes.isEmpty()) {
			return yearMonthCheckBoxes;
		}
		yearMonthCheckBoxes = new ArrayList<>();
		String sql = """
				SELECT ym.ym_id, yc.year, mc.month
				FROM yearmonth ym
				INNER JOIN yearchoice yc ON ym.y_id = yc.y_id
				INNER JOIN monthchoice mc ON ym.m_id = mc.m_id
				""";

		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();

		while (rs.next()) {
			int ymId = rs.getInt("ym_id");
			String year = rs.getString("year");
			String month = rs.getString("month");

			CheckBox checkBox = new CheckBox(year + " " + month + " " + "Exam");
			checkBox.setStyle("-fx-padding:0 10;");
			checkBox.setCursor(Cursor.HAND);
			checkBox.setId(String.valueOf(ymId));
			yearMonthCheckBoxes.add(checkBox);

		}

		if (scrollPane != null) {
			VBox vbox = new VBox(10);
			Label titleLabel = new Label("Select Quiz Sets");
			titleLabel.setStyle(
					"-fx-font-size:  20px; -fx-font-style:  italic; -fx-font-family:System; -fx-padding:7 0 3 10; ");
			CheckBox chkAll = new CheckBox("Select / Deselect All");
			chkAll.setStyle("-fx-padding:0 10;");
			chkAll.setCursor(Cursor.HAND);
			chkAll.setOnAction(event -> {
				boolean isSelected = chkAll.isSelected();
				for (CheckBox cb : yearMonthCheckBoxes) {
					cb.setSelected(isSelected);
				}
			});
			vbox.getChildren().add(titleLabel);
			vbox.getChildren().add(chkAll);
			vbox.getChildren().addAll(yearMonthCheckBoxes);
			scrollPane.setContent(vbox);
			scrollPane.setFitToWidth(true);
		}

		return yearMonthCheckBoxes;
	}

	public List<CheckBox> chapterCheckBox() throws SQLException {
		if (!chapterCheckBoxes.isEmpty()) {
			return chapterCheckBoxes;
		}
		chapterCheckBoxes = new ArrayList<>();
		String sql = "SELECT * FROM chapter";

		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();

		while (rs.next()) {
			String chapter = rs.getString("chapter_name");
			int chapterId = rs.getInt("c_id"); // Assuming chapter_id is the primary key

			CheckBox checkBox = new CheckBox(chapter);
			checkBox.setId(String.valueOf(chapterId)); // Set a valid ID
			checkBox.setStyle("-fx-padding:0 10;");
			checkBox.setCursor(Cursor.HAND);
			chapterCheckBoxes.add(checkBox);
		}

		if (chScrollPane != null) {
			VBox vbox = new VBox(10);
			Label titleLabel = new Label("Select Categories");
			titleLabel.setStyle(
					"-fx-font-size:  20px; -fx-font-style:  italic; -fx-font-family:System; -fx-padding:7 0 3 10; ");
			CheckBox chkAll = new CheckBox("Select / Deselect All");
			chkAll.setStyle("-fx-padding:0 10;");
			chkAll.setCursor(Cursor.HAND);
			chkAll.setOnAction(event -> {
				boolean isSelected = chkAll.isSelected();
				for (CheckBox cb : chapterCheckBoxes) {
					cb.setSelected(isSelected);
				}
			});

			vbox.getChildren().add(titleLabel);
			vbox.getChildren().add(chkAll);
			vbox.getChildren().addAll(chapterCheckBoxes);
			chScrollPane.setContent(vbox);
			chScrollPane.setFitToWidth(true);
		}

		return chapterCheckBoxes;
	}

	public void checkSelectionAndShowAlert() {
		boolean ymSelected = false;
		boolean chSelected = false;

		for (CheckBox cb : yearMonthCheckBoxes) {
			if (cb.isSelected()) {
				ymSelected = true;
				break;
			}
		}

		for (CheckBox cb : chapterCheckBoxes) {
			if (cb.isSelected()) {
				chSelected = true;
				break;
			}
		}

		if (ymSelected && chSelected) {
		} else if (!ymSelected && !chSelected) {
			showAlert(AlertType.WARNING, "Selection Required", "No Selection",
					"You must select at least one Year-Month and one Chapter checkbox.");
		} else if (!ymSelected) {
			showAlert(AlertType.WARNING, "Selection Required", "No Year-Month Selected",
					"Please select at least one Year-Month checkbox.");
		} else {
			showAlert(AlertType.WARNING, "Selection Required", "No Chapter Selected",
					"Please select at least one Chapter checkbox.");
		}
	}

	private void showAlert(AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private void showAlert(String title, String message) {
		javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public Map<Integer, String> getUserAnswerSelections() {
		return userAnswerSelections;
	}

	public Set<Integer> getAnsweredQuestions() {
		return answeredQuestions;
	}

	public void setUserAnswerSelections(Map<Integer, String> userAnswerSelections) {
		FeAmChController.userAnswerSelections = userAnswerSelections;
	}
}
