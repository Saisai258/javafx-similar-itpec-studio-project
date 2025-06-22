package application.teacher.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.main.DatabaseConnection;
import application.teacher.Question;
import application.teacher.form.QuestionFEForm;
import application.teacher.form.QuestionForm;
import application.teacher.form.UpdateQuestionAllForm;
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

public class UpdateQuestionFormAllController implements Initializable {
	@FXML
	private ComboBox<String> chapter, year, type, month;

	@FXML
	private ComboBox<String> etype;
	@FXML
	private TextArea question;
	@FXML
	private TextArea answerA;
	@FXML
	private TextArea answerB;
	@FXML
	private TextArea answerC;
	@FXML
	private TextArea answerD;
	@FXML
	private TextArea correctAnswer;
	@FXML
	private TextField qNo;
	@FXML
	private ImageView qimage, aImage, bImage, cImage, dImage, ansImage;
	private File questionImage, answerAImg, answerBImg, answerCImg, answerDImg, correctImg;
	int tid = -1;
	int ttid = -1;
	@FXML
	private List<File> answerImages = new ArrayList<>(); // List to store selected answer images
	@FXML
	private List<String> answerText = new ArrayList<>();
	private int qNumber;
	Question updQues = BeforeUpdateQuestionFormControllerCopy.update_Question;
	public String section;

	@FXML
	void btnquestionUpdate(ActionEvent event) {
		try {
			if (validateAnswers()) {
				updateData();
				qNo.setText(null);
				question.setText(null);
				answerA.setText(null);
				answerB.setText(null);
				answerC.setText(null);
				answerD.setText(null);
				correctAnswer.setText(null);
				questionImage = null;
				answerAImg = null;
				answerBImg = null;
				answerCImg = null;
				answerDImg = null;
				correctImg = null;
				qimage.setImage(null);
				aImage.setImage(null);
				bImage.setImage(null);
				cImage.setImage(null);
				dImage.setImage(null);
				ansImage.setImage(null);
				etype.setValue(null);
				year.setValue(null);
				month.setValue(null);
				type.setValue(null);
				chapter.setValue(null);
				method();
			}
		} catch (Exception e) {
			e.printStackTrace();
			showAlert("Error", "Failed to add question", "An error occurred while adding the question.");
		}
	}

//	-------------------------image browse start ------------------------------
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
			qimage.setImage(image);
		}
	}

	@FXML
	void browseA(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");

		// Set extension filter
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
		fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

		// Show open file dialog
		answerAImg = fileChooser.showOpenDialog(null);
		if (answerAImg != null) {
			// System.out.println(selectedFile.getAbsolutePath());
			Image image = new Image(answerAImg.getAbsolutePath());
			aImage.setImage(image);
			answerImages.add(answerAImg);
		}
	}

	@FXML
	void browseB(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");

		// Set extension filter
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
		fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

		// Show open file dialog
		answerBImg = fileChooser.showOpenDialog(null);
		if (answerBImg != null) {
			// System.out.println(selectedFile.getAbsolutePath());
			Image image = new Image(answerBImg.getAbsolutePath());
			bImage.setImage(image);
			answerImages.add(answerBImg);
		}
	}

	@FXML
	void browseC(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");
		// Set extension filter
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
		fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
		// Show open file dialog
		answerCImg = fileChooser.showOpenDialog(null);
		if (answerCImg != null) {
			// System.out.println(selectedFile.getAbsolutePath());
			Image image = new Image(answerCImg.getAbsolutePath());
			cImage.setImage(image);
			answerImages.add(answerCImg);
		}
	}

	@FXML
	void browseD(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");
		// Set extension filter
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
		fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
		// Show open file dialog
		answerDImg = fileChooser.showOpenDialog(null);
		if (answerDImg != null) {
			// System.out.println(selectedFile.getAbsolutePath());
			Image image = new Image(answerDImg.getAbsolutePath());
			dImage.setImage(image);
			answerImages.add(answerDImg);
		}
	}

	@FXML
	void BrowseAns(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");
		// Set extension filter
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
		fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
		// Show open file dialog
		correctImg = fileChooser.showOpenDialog(null);
		if (correctImg != null) {
//			 System.out.println(correctImg.getAbsolutePath());
			Image image = new Image(correctImg.getAbsolutePath());
			ansImage.setImage(image);
		}
	}

	@FXML
	void btnACancle(ActionEvent event) {
		answerAImg = null;
		aImage.setImage(null);

	}

	@FXML
	void btnBCancle(ActionEvent event) {
		answerBImg = null;
		bImage.setImage(null);

	}

	@FXML
	void btnCCancle(ActionEvent event) {
		answerCImg = null;
		cImage.setImage(null);

	}

	@FXML
	void btnDCancle(ActionEvent event) {
		answerDImg = null;
		dImage.setImage(null);

	}

	@FXML
	void btnAnsCancle(ActionEvent event) {
		correctImg = null;
		ansImage.setImage(null);

	}

	@FXML
	void btnQCancle(ActionEvent event) {
		questionImage = null;
		qimage.setImage(null);

	}

//	-------------------------image browse end ------------------------------
//	-------------------------ID get start ------------------------------

//	-------------------------validation and addition start -------------------------
	private void setCorrectAnswerText() {
		if (correctImg != null && Optional.ofNullable(correctAnswer.getText()).orElse("").trim().isEmpty()) {
			if (isSameImage(correctImg, answerAImg))
				correctAnswer.setText("A");
			else if (isSameImage(correctImg, answerBImg))
				correctAnswer.setText("B");
			else if (isSameImage(correctImg, answerCImg))
				correctAnswer.setText("C");
			else if (isSameImage(correctImg, answerDImg))
				correctAnswer.setText("D");

		} else if (correctImg == null && !Optional.ofNullable(correctAnswer.getText()).orElse("").trim().isEmpty()) {
			if (isSameText(correctAnswer, answerA) && answerAImg != null) {
				warningInfo("Error", "Answer Image Mismatch", "check correct answer image and answerA's image ");
				return;
			} else if (isSameText(correctAnswer, answerB) && answerBImg != null) {
				warningInfo("Error", "Answer Image Mismatch", "check correct answer image and answerB's image ");
				return;
			} else if (isSameText(correctAnswer, answerC) && answerCImg != null) {
				warningInfo("Error", "Answer Image Mismatch", "check correct answer image and answerC's image ");
				return;
			} else if (isSameText(correctAnswer, answerD) && answerDImg != null) {
				warningInfo("Error", "Answer Image Mismatch", "check correct answer image and answerD's image ");
				return;
			}
		}
	}

	private boolean isCorrectAnswerMatching() {
		if (correctImg != null && !Optional.ofNullable(correctAnswer.getText()).orElse("").trim().isEmpty()) {
			// String correctText = correctAnswer.getText().trim();

			if (isSameImage(correctImg, answerAImg) && !isSameText(correctAnswer, answerA)) {
				warningInfo("Error", "Missing Match", "Answer A text mismatch.");
				return false;
			}
			if (!isSameImage(correctImg, answerAImg) && isSameText(correctAnswer, answerA)) {
				warningInfo("Error", "Missing Match", "Answer A image mismatch.");
				return false;
			}
			if (isSameImage(correctImg, answerBImg) && !isSameText(correctAnswer, answerB)) {
				warningInfo("Error", "Missing Match", "Answer B text mismatch.");
				return false;
			}
			if (!isSameImage(correctImg, answerBImg) && isSameText(correctAnswer, answerB)) {
				warningInfo("Error", "Missing Match", "Answer B image mismatch.");
				return false;
			}
			if (isSameImage(correctImg, answerCImg) && !isSameText(correctAnswer, answerC)) {
				warningInfo("Error", "Missing Match", "Answer C text mismatch.");
				return false;
			}
			if (!isSameImage(correctImg, answerCImg) && isSameText(correctAnswer, answerC)) {
				warningInfo("Error", "Missing Match", "Answer C image mismatch.");
				return false;
			}
			if (isSameImage(correctImg, answerDImg) && !isSameText(correctAnswer, answerD)) {
				warningInfo("Error", "Missing Match", "Answer D text mismatch.");
				return false;
			}
			if (!isSameImage(correctImg, answerDImg) && isSameText(correctAnswer, answerD)) {
				warningInfo("Error", "Missing Match", "Answer D image mismatch.");
				return false;
			}
		}
		return true;
	}

	private void autoFillTextAnswers() {
		answerText.clear(); // Clear old values before adding new ones
		if (answerAImg != null && Optional.ofNullable(answerA.getText()).orElse("").trim().isEmpty()) {
			answerA.setText("A");
		}
		answerText.add(answerA.getText().trim());
		if (answerBImg != null && Optional.ofNullable(answerB.getText()).orElse("").trim().isEmpty()) {
			answerB.setText("B");
		}
		answerText.add(answerB.getText().trim());
		if (answerCImg != null && Optional.ofNullable(answerC.getText()).orElse("").trim().isEmpty()) {
			answerC.setText("C");
		}
		answerText.add(answerC.getText().trim());
		if (answerDImg != null && Optional.ofNullable(answerD.getText()).orElse("").trim().isEmpty()) {
			answerD.setText("D");
		}
		answerText.add(answerD.getText().trim());
		System.out.println(answerText);
	}

	private List<String> questionNumberList = new ArrayList<>(); // Ensure it's initialized

	private boolean validateQuestionNumber() {
		String text = qNo.getText().trim(); // Trim to remove spaces

		if (text.isEmpty()) { // Check if empty first
			warningInfo("Error", "Missing Addition", "Please add values for question number!");
			return false;
		}

		try {
			qNumber = Integer.parseInt(text); // Convert only if it's not empty
			if (section == "PM" && qNumber > 20) {
				warningInfo("Error", "Invalid Input",
						"Question Number is over (20). Qusetion type is FE->evening(PM) section!!");
				return false;
			} else if (qNumber > 100) {
				warningInfo("Error", "Invalid Input", "Number must be 100 or less.");
				return false;
			}
			return true; // Valid number
		} catch (NumberFormatException e) {
			warningInfo("Error", "Invalid Input", "Please enter a valid number for the question.");
			return false;
		}
	}

	private boolean validatePresence() {
		if (isNullOrEmpty(answerAImg, answerA))
			return showWarning("Answer (A)");
		if (isNullOrEmpty(answerBImg, answerB))
			return showWarning("Answer (B)");
		if (isNullOrEmpty(answerCImg, answerC))
			return showWarning("Answer (C)");
		if (isNullOrEmpty(answerDImg, answerD))
			return showWarning("Answer (D)");
		if (isNullOrEmpty(correctImg, correctAnswer))
			return showWarning("Correct Answer");
		if (isNullOrEmpty(questionImage, question))
			return showWarning("Question");
		return true;
	}

	private boolean isNullOrEmpty(File image, TextArea answerText) {
		return image == null && (answerText == null || answerText.getText().trim().isEmpty());
	}

	private boolean showWarning(String fieldName) {
		warningInfo("Error", fieldName + " not found", "Please add your " + fieldName + " as an Image or Text!");
		return false;
	}

	private boolean validateDuplicates() {
		if (hasDuplicateImages())
			return false;
		if (hasDuplicateTexts())
			return false;
		return true;
	}

	private boolean hasDuplicateImages() {
		return checkDuplicateImages(answerAImg, answerBImg, "A", "B")
				|| checkDuplicateImages(answerAImg, answerCImg, "A", "C")
				|| checkDuplicateImages(answerAImg, answerDImg, "A", "D")
				|| checkDuplicateImages(answerBImg, answerCImg, "B", "C")
				|| checkDuplicateImages(answerBImg, answerDImg, "B", "D")
				|| checkDuplicateImages(answerCImg, answerDImg, "C", "D")
				|| checkDuplicateImages(questionImage, answerAImg, "Question", "A")
				|| checkDuplicateImages(questionImage, answerBImg, "Question", "B")
				|| checkDuplicateImages(questionImage, answerCImg, "Question", "C")
				|| checkDuplicateImages(questionImage, answerDImg, "Question", "D");
	}

	private boolean hasDuplicateTexts() {
		return checkDuplicateTexts(answerA, answerB, "A", "B") || checkDuplicateTexts(answerA, answerC, "A", "C")
				|| checkDuplicateTexts(answerA, answerD, "A", "D") || checkDuplicateTexts(answerB, answerC, "B", "C")
				|| checkDuplicateTexts(answerB, answerD, "B", "D") || checkDuplicateTexts(answerC, answerD, "C", "D")
				|| checkDuplicateTexts(question, answerA, "Question", "A")
				|| checkDuplicateTexts(question, answerB, "Question", "B")
				|| checkDuplicateTexts(question, answerC, "Question", "C")
				|| checkDuplicateTexts(question, answerD, "Question", "D");
	}

	private boolean checkDuplicateImages(File img1, File img2, String label1, String label2) {
		if (isSameImage(img1, img2)) {
			warningInfo("Error", "Answer Image Duplicate",
					"Please check your answer image data of (" + label1 + ") and (" + label2 + ")!.");
			return true;
		}
		return false;
	}

	private boolean checkDuplicateTexts(TextArea answerA2, TextArea answerB2, String label1, String label2) {
		if (answerA2.getText().trim().equalsIgnoreCase(answerB2.getText().trim())) {
			warningInfo("Error", "Answer Text Duplicate",
					"Please check your answer textfield data of (" + label1 + ") and (" + label2 + ")!.");
			return true;
		}
		return false;
	}

	private boolean validateSelections() {
		// Validate combo box selections
		if (year.getValue() == null || month.getValue() == null || chapter.getValue() == null || type.getValue() == null
				|| etype.getValue() == null) {
			warningInfo("Error", "Missing Selection", "Please select values for Year, Month, Chapter, and Type.");
			return false;
		}

		// Validate answer text
		String correctText = correctAnswer.getText().trim();
		if (!answerText.contains(correctText)) {
			warningInfo("Error", "Answer Text Mismatch", "Please check your correct answer text.");
			return false;
		}

		return true;
	}

	private boolean isSameImage(File img1, File img2) {
		if (img1 == null || img2 == null) {
			return false;
		}

		try {
			// Compare file contents byte-by-byte
			if (img1.length() != img2.length()) {
				return false;
			}

			try (InputStream is1 = new FileInputStream(img1); InputStream is2 = new FileInputStream(img2)) {
				int byte1, byte2;
				do {
					byte1 = is1.read();
					byte2 = is2.read();
					if (byte1 != byte2) {
						return false;
					}
				} while (byte1 != -1);

				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean isSameText(TextArea text1, TextArea text2) {
		return Optional.ofNullable(text1.getText()).orElse("").trim()
				.equalsIgnoreCase(Optional.ofNullable(text2.getText()).orElse("").trim());
	}

//	valadition of all method
	private boolean validateAnswers() throws ClassNotFoundException, SQLException {
		// Validate presence
		if (!validatePresence()) {
			return false;
		}

		// Validate question number
		if (!validateQuestionNumber()) {
			return false;
		}

		// Auto-fill text answers
		autoFillTextAnswers();

		// Set the correct answer text
		setCorrectAnswerText();

		// Validate duplicates
		if (!validateDuplicates()) {
			return false;
		}

		// Validate selections
		if (!validateSelections()) {
			return false;
		}

		// Check for duplicate question numbers
		if (!duplicateCheckQuestionNumber()) {
			return false;
		}

		// Check if the image and text match any answer
		if (!isCorrectAnswerMatching()) {
			return false;
		}

		System.out.println("No duplicate found. Proceeding with insert...");
		return true;
	}

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
				examtypeList.add(value);
			}
		}
		etype.getItems().setAll(examtypeList);

		// Step 2: When exam type is selected → load related types
		etype.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

			if (newValue == null || section == null) {
				try {
					section = getExamSectionName(updQues.getEsection_id());
					System.out.println("592 section name : " + section);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return;
			} else if ("FE".equals(newValue)) {
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
			} else {
				section = "IP";
			}
			try {
				int tid = 0;
				try (PreparedStatement pst = con.prepareStatement("SELECT exam_id FROM examtype WHERE exam_name = ?")) {
					pst.setString(1, newValue);
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							tid = rs.getInt("exam_id");
							System.out.println("673 exam_id : " + tid);
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
									value = value.substring(0, 1).toUpperCase() + value.substring(1); // Capitalize
																										// first letter
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
					try (PreparedStatement pst = con
							.prepareStatement("SELECT DISTINCT month FROM monthchoice WHERE m_id = ?")) {
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
		return input == null ? "" : input.trim().replaceAll("\\s+", " "); // normalize multiple spaces
	}

	private void updateData() throws SQLException, IOException, ClassNotFoundException {
		int questionNumber;

		// 1. Validate question number input
		try {
			questionNumber = Integer.parseInt(qNo.getText().trim());
			if (questionNumber < 1 || questionNumber > 100) {
				showAlert("Invalid Input", "Question Number Out of Range", "Please enter a number between 1 and 100.");
				return;
			}
		} catch (NumberFormatException e) {
			showAlert("Invalid Input", "Invalid Number", "Please enter a valid number between 1 and 100.");
			return;
		}

		// 2. Get existing question and image IDs
		int imgId = updQues.getImg_id();
		int qId = updQues.getQ_id();
		if (qId == 0) {
			showAlert("Error", "Not Found", "No existing question found to update.");
			return;
		}

		DatabaseConnection db = new DatabaseConnection();

		try (Connection con = db.getConnetion()) {
			con.setAutoCommit(false); // Start transaction

			// 3. Update the question table
			String questionSQL = "UPDATE ipquestion SET q_no=?, description=?, answer_a=?, answer_b=?, answer_c=?, answer_d=?, answer=?, img_id=?, c_id=?, t_id=?, ym_id=?, exam_id=?, esection_id=? WHERE Qip_id=?";
			try (PreparedStatement pst = con.prepareStatement(questionSQL)) {
				pst.setInt(1, questionNumber);
				pst.setString(2, question.getText());
				pst.setString(3, answerA.getText());
				pst.setString(4, answerB.getText());
				pst.setString(5, answerC.getText());
				pst.setString(6, answerD.getText());
				pst.setString(7, correctAnswer.getText());
				pst.setInt(8, imgId);
				pst.setInt(9, getCID());
				pst.setInt(10, getTID());
				pst.setInt(11, getYMID());
				pst.setInt(12, getETID());
				pst.setInt(13, getExamSectionID());
				pst.setInt(14, qId);
				pst.executeUpdate();
			}

			// 4. Update the questionimage table
			String imageSQL = "UPDATE questionimage SET q_image=?, ans_img_a=?, ans_img_b=?, ans_img_c=?, ans_img_d=?, ans_cor_img=? WHERE img_id=?";
			try (PreparedStatement pst = con.prepareStatement(imageSQL)) {
				pst.setBinaryStream(1, questionImage != null ? new FileInputStream(questionImage) : null);
				pst.setBinaryStream(2, answerAImg != null ? new FileInputStream(answerAImg) : null);
				pst.setBinaryStream(3, answerBImg != null ? new FileInputStream(answerBImg) : null);
				pst.setBinaryStream(4, answerCImg != null ? new FileInputStream(answerCImg) : null);
				pst.setBinaryStream(5, answerDImg != null ? new FileInputStream(answerDImg) : null);
				pst.setBinaryStream(6, correctImg != null ? new FileInputStream(correctImg) : null);
				pst.setInt(7, imgId);
				pst.executeUpdate();
			}

			// 5. Commit changes and show success message
			con.commit();
			showAlert("Success", "Question Updated", "The question has been successfully updated.");
			UpdateQuestionAllForm.uqf.close();

		} catch (Exception e) {
			e.printStackTrace();
			showAlert("Error", "Update Failed", "An error occurred while updating the question:\n" + e.getMessage());
		}
	}

	private int getExamSectionID() {
		int sectionId = 0;

		try {
			if (section != null && !section.isEmpty()) {
				System.out.println("932 third section : " + section);
				DatabaseConnection db = new DatabaseConnection();
				try (Connection con = db.getConnetion();
						PreparedStatement pst = con
								.prepareStatement("SELECT esection_id FROM examsection WHERE section_name = ?")) {
					pst.setString(1, section);
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							sectionId = rs.getInt("esection_id");
							System.out.println("285 Fetched esection_id from DB: " + sectionId);
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return sectionId;
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

	private int getETID() {
		int examId = 0;

		try {
			String selectedExamName = etype.getValue();

			if (selectedExamName != null && !selectedExamName.isEmpty()) {
				// Try to fetch exam_id from DB
				DatabaseConnection db = new DatabaseConnection();
				try (Connection con = db.getConnetion();
						PreparedStatement pst = con
								.prepareStatement("SELECT exam_id FROM examtype WHERE exam_name = ?")) {

					pst.setString(1, selectedExamName);

					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							examId = rs.getInt("exam_id");
						}
					}
				}
			}

			// Fallback if no valid exam_id was fetched
			if (examId == 0) {
				examId = updQues.getExam_id();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return examId;
	}

//	-------------------------ID get end ------------------------------	

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

	private String getChapterName(int c_id) throws SQLException {
		String query = "SELECT chapter_name FROM chapter WHERE c_id = ?";

		try (Connection conn = new DatabaseConnection().getConnetion();
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, c_id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("chapter_name");
			}
		}

		return null;
	}

	private String getTypeCategory(int t_id) throws SQLException {
		String query = "SELECT category FROM qtype WHERE t_id = ?";

		try (Connection conn = new DatabaseConnection().getConnetion();
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, t_id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("category");
			}
		}

		return null;
	}

	private String getExamSectionName(int esection_id) throws SQLException {
		String query = "SELECT section_name FROM examsection WHERE esection_id = ?";

		try (Connection conn = new DatabaseConnection().getConnetion();
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, esection_id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("section_name");
			}
		}

		return null;
	}

	private String getYear(int ym_id) throws SQLException {
		String query = "SELECT y.year FROM yearmonth ym JOIN yearchoice y ON ym.y_id = y.y_id WHERE ym.ym_id = ?";

		try (Connection conn = new DatabaseConnection().getConnetion();
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, ym_id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("year");
			}
		}

		return null;
	}

	private String getMonth(int ym_id) throws SQLException {
		String query = "SELECT m.month FROM yearmonth ym JOIN monthchoice m ON ym.m_id = m.m_id WHERE ym.ym_id = ?";

		try (Connection conn = new DatabaseConnection().getConnetion();
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, ym_id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("month");
			}
		}

		return null;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		method();

		if (updQues != null) {
			try {

				// ✅ Capture and store IDs
				chapter.setValue(getChapterName(updQues.getC_id()));
				type.setValue(getTypeCategory(updQues.getT_id()));
				year.setValue(getYear(updQues.getYm_id()));
				month.setValue(getMonth(updQues.getYm_id()));
				etype.setValue(getExamSectionName(updQues.getEsection_id()));

				// ✅ Set text fields
				qNo.setText(Integer.toString(updQues.getQNo()));
				question.setText(updQues.getQ_desc());
				answerA.setText(updQues.getAns_a());
				answerB.setText(updQues.getAns_b());
				answerC.setText(updQues.getAns_c());
				answerD.setText(updQues.getAns_d());
				correctAnswer.setText(updQues.getQ_ans());

				// ✅ Load images
				setImage(qimage, updQues.getQ_descimg(), questionImage);
				setImage(aImage, updQues.getAns_aimg(), answerAImg);
				setImage(bImage, updQues.getAns_bimg(), answerBImg);
				setImage(cImage, updQues.getAns_cimg(), answerCImg);
				setImage(dImage, updQues.getAns_dimg(), answerDImg);
				setImage(ansImage, updQues.getQ_ansimg(), correctImg);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Error: updQues is null.");
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

	private void setImage(ImageView imageView, byte[] imageData, File originalFile) {
		if (imageData != null) {
			try {
				imageView.setImage(new Image(new ByteArrayInputStream(imageData)));

				// Preserve original file reference if available
				if (originalFile != null) {
					if (imageView == qimage)
						questionImage = originalFile;
					else if (imageView == aImage)
						answerAImg = originalFile;
					// ... other image views
				}
			} catch (Exception e) {
				e.printStackTrace();
				imageView.setImage(null);
			}
		} else {
			imageView.setImage(null);
		}
		if (imageData != null) {
			try {
				// Create temporary file
				File tempFile = File.createTempFile("tempImg", ".tmp");
				tempFile.deleteOnExit(); // Ensures file is deleted on JVM exit

				try (FileOutputStream fos = new FileOutputStream(tempFile)) {
					fos.write(imageData);
				}

				// Display image
				imageView.setImage(new Image(new ByteArrayInputStream(imageData)));

				// Store file reference
				if (imageView == qimage) {
					questionImage = tempFile;
				} else if (imageView == aImage) {
					answerAImg = tempFile;
				} else if (imageView == bImage) {
					answerBImg = tempFile;
				} else if (imageView == cImage) {
					answerCImg = tempFile;
				} else if (imageView == dImage) {
					answerDImg = tempFile;
				} else if (imageView == ansImage) {
					correctImg = tempFile;
				}
			} catch (IOException e) {
				e.printStackTrace(); // Optionally: show dialog or log more
				imageView.setImage(null);
			}
		} else {
			imageView.setImage(null);

			// Optional: clear file reference
			if (imageView == qimage)
				questionImage = null;
			else if (imageView == aImage)
				answerAImg = null;
			else if (imageView == bImage)
				answerBImg = null;
			else if (imageView == cImage)
				answerCImg = null;
			else if (imageView == dImage)
				answerDImg = null;
			else if (imageView == ansImage)
				correctImg = null;
		}
	}

	@FXML
	void btnCancel(ActionEvent event) {
		UpdateQuestionAllForm.uqf.close();
	}

	private boolean duplicateCheckQuestionNumber() throws SQLException, ClassNotFoundException {
		getQNO();

		// Ensure that the input question number is valid (non-null and non-empty)
		String questionNumber = qNo.getText();
		if (questionNumber == null || questionNumber.trim().isEmpty()) {
			warningInfo("Error", "Invalid Question Number", "The question number cannot be empty.");
			return false; // Invalid input case
		}

		// Check for duplicates in the list, excluding the current question
		if (questionNumberList.contains(questionNumber)) {
			// If the duplicate is the current question's number, that's okay (we're editing
			// it)
			if (updQues != null && Integer.parseInt(questionNumber) == updQues.getQNo()) {
				return true;
			}
			warningInfo("Error", "Duplicate Question Number",
					"A question number for this year and month already exists.");
			return false;
		}

		return true; // No duplicate found
	}

	public void getQNO() throws SQLException, ClassNotFoundException {
		// Step 1: Get ym_id from the yearmonth table
		int ym_id = getYMID();
		if (ym_id == -1) {
			System.out.println("No matching ym_id found for the given year and month.");
			return;
		}

		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		String query = "SELECT q_no FROM ipquestion WHERE ym_id = ? and esection_id=?";

		try (PreparedStatement pst = con.prepareStatement(query)) {
			pst.setInt(1, ym_id);
			pst.setInt(2, getExamSectionID());

			try (ResultSet rs = pst.executeQuery()) {
				questionNumberList.clear(); // Clear previous results before adding new ones
				while (rs.next()) {
					// Skip the current question's number if we're editing it
					if (updQues != null && rs.getInt("q_no") == updQues.getQNo()) {
						continue;
					}
					questionNumberList.add(rs.getString("q_no"));
				}
			}
		} catch (SQLException e) {
			System.err.println("SQL Error in getQNO(): " + e.getMessage());
			e.printStackTrace();
		}
	}
}
