package application.teacher.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class QuesFileGenerateController implements Initializable {

	public static int examTotalMark;
	public static int yearMonthCBO;
	public static int[] answerUser;
	public static int k = 0;
	public static List<Question> questionList;
	List<Integer> chapterCheckId;
	List<Integer> yearmonthCheckId;
	private List<CheckBox> chapterCheckBoxes = new ArrayList<>();
	private List<CheckBox> yearMonthCheckBoxes = new ArrayList<>();
	private int selectedEsectionId = 0;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private RadioButton rbAm;

	@FXML
	private RadioButton rbIp;

	@FXML
	private RadioButton rbPm;

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

		ToggleGroup sectionToggleGroup = new ToggleGroup();
		rbIp.setToggleGroup(sectionToggleGroup);
		rbAm.setToggleGroup(sectionToggleGroup);
		rbPm.setToggleGroup(sectionToggleGroup);

		// Fix: Set section selection listeners here (NOT in forQuesUpdate)
		rbIp.setOnAction(e -> selectedEsectionId = 1);
		rbAm.setOnAction(e -> selectedEsectionId = 2);
		rbPm.setOnAction(e -> selectedEsectionId = 3);

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
	void viewLinkAction(ActionEvent event) throws SQLException {
		// Check selections and show alerts if needed
		checkSelectionAndShowAlert();

		if (selectedEsectionId == 0) {
			showAlert(Alert.AlertType.WARNING, "Selection Required", "No Section Selected",
					"Please select a section (IP, AM, or PM) before proceeding.");
			return;
		}
		if (selectedEsectionId == 3) {
			showAlert(Alert.AlertType.INFORMATION, "Update Blocked", "PM Section Editing",
					"Editing FE PM questions is under maintenance.");
			return;
		}

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
			stage.setTitle("Questions");
			stage.show();
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert("Database Error", "An error occurred while loading the question list. Please try again later.");
		} catch (Exception e) {
			e.printStackTrace();
			showAlert("Error", "An unexpected error occurred. Please try again.");
		}
	}

	private InputStream fetchImageFromDB(int imgId, String columnName) throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		try (Connection con = db.getConnetion();
				PreparedStatement stmt = con.prepareStatement("SELECT " + columnName + " FROM questionImage WHERE img_id = ?")) {

			stmt.setInt(1, imgId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					byte[] imageBytes = rs.getBytes(columnName); // Fully read the bytes
					if (imageBytes != null) {
						return new ByteArrayInputStream(imageBytes); // Return a stream from byte[]
					}
				}
			}
		}
		return null;
	}


	private void addImageToDocx(XWPFDocument document, InputStream imageStream)
			throws IOException, InvalidFormatException {

		if (imageStream != null) {
			// Read image into a BufferedImage to get dimensions
			BufferedImage image = ImageIO.read(imageStream);
			if (image == null)
				return;

			int originalWidth = image.getWidth();
			int originalHeight = image.getHeight();

			// Define maximum dimensions (in pixels)
			int maxWidth = 400;
			int maxHeight = 300;

			// Scale proportionally
			float widthScale = (float) maxWidth / originalWidth;
			float heightScale = (float) maxHeight / originalHeight;
			float scale = Math.min(widthScale, heightScale);

			int scaledWidth = (int) (originalWidth * scale);
			int scaledHeight = (int) (originalHeight * scale);

			// Convert image to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());

			// Create paragraph and insert picture
			XWPFParagraph imagePara = document.createParagraph();
			imagePara.setAlignment(ParagraphAlignment.CENTER); // Center image (optional)
			XWPFRun imageRun = imagePara.createRun();
			imageRun.addPicture(bis, XWPFDocument.PICTURE_TYPE_PNG, "image.png", Units.toEMU(scaledWidth),
					Units.toEMU(scaledHeight));

			bis.close();
			baos.close();
		}
	}

	public ScrollPane showQuestionList() throws SQLException {
		VBox mainVBox = new VBox();
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(mainVBox);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setPrefSize(1366, 700);

		mainVBox.setSpacing(10);
		mainVBox.setPrefSize(1366, 700);
		mainVBox.setAlignment(Pos.TOP_CENTER);
		mainVBox.setStyle("-fx-background-color:lightblue;");

		// Create the header
		Label headerLabel = new Label("Generate Questions To Document File");
		headerLabel.setStyle(
				"-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: navy; -fx-font-family:Montserrat;");
		headerLabel.setAlignment(Pos.CENTER);

		// Add the header to the top of the main VBox
		mainVBox.getChildren().add(headerLabel);

		VBox vboxQuestion = new VBox();
		vboxQuestion.setSpacing(10);
		vboxQuestion.setPrefWidth(1366);
		vboxQuestion.setAlignment(Pos.TOP_CENTER);

		getQueryQuestion();// add question List

		// Check if "Random" checkbox is selected
		if (chkRandom.isSelected()) {
			// Shuffle the questions randomly
			Collections.shuffle(questionList);
		}

		// Limit questions to the spinner value
		int questionLimit = qSpinner.getValue();
		int totalQuestions = Math.min(questionList.size(), questionLimit);
		List<Question> limitedQuestions = questionList.subList(0, totalQuestions);

		int rowCount = limitedQuestions.size(); // Fixed to use limitedQuestions
		String[] answerKeyGroup = new String[rowCount];
		answerUser = new int[rowCount];
		int ansNo = 0;

		for (int i = 0; i < limitedQuestions.size(); i++) { // Use limitedQuestions.size()
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
		Button btnGenerate = new Button("Generate");
		btnGenerate.setCursor(Cursor.HAND);
		btnGenerate.setStyle(
				"-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px;");

		// Arrange the buttons horizontally using HBox
		HBox buttonBox = new HBox(20); // Adds spacing between buttons
		buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
		buttonBox.setPadding(new Insets(50));
		buttonBox.getChildren().addAll(btnCancel, btnGenerate);
		mainVBox.getChildren().add(buttonBox);

		// Generate button action
		btnGenerate.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Files", "*.docx"));
			fileChooser.setTitle("Save Questions as DOCX");

			File file = fileChooser.showSaveDialog(btnGenerate.getScene().getWindow());
			if (file != null) {
				try (XWPFDocument document = new XWPFDocument()) {
					// Create a paragraph for the top section
					XWPFParagraph topPara = document.createParagraph();
					topPara.setAlignment(ParagraphAlignment.CENTER);

					// Create a table for header layout (1 row, 3 cells)
					XWPFTable headerTable = document.createTable(1, 3);
					headerTable.setWidth("100%");
					headerTable.getCTTbl().getTblPr().unsetTblBorders();

					XWPFTableRow headerRow = headerTable.getRow(0);

					// === Left Cell: MBTU Logo ===
					XWPFTableCell leftCell = headerRow.getCell(0);
					XWPFParagraph leftPara = leftCell.getParagraphs().get(0);
					leftPara.setAlignment(ParagraphAlignment.LEFT);
					leftCell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000)); // 20% of table width
					leftCell.getCTTc().getTcPr().getTcW().setType(STTblWidth.PCT);// to set with %
					XWPFRun leftRun = leftPara.createRun();
					try (InputStream mbtuLogo = new FileInputStream(
							"D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images\\header22.png")) {
						leftRun.addPicture(mbtuLogo, Document.PICTURE_TYPE_PNG, "mbtu.png", Units.toEMU(80),
								Units.toEMU(60));
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					String Sclass = "";
					if (selectedEsectionId == 1) {
						Sclass = "IP";
					} else if (selectedEsectionId == 2) {
						Sclass = "FE";
					}

					// === Center Cell: Text Info ===
					XWPFTableCell centerCell = headerRow.getCell(1);
					XWPFParagraph centerPara = centerCell.getParagraphs().get(0);
					centerCell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(6000)); // 60% of table width
					centerCell.getCTTc().getTcPr().getTcW().setType(STTblWidth.PCT);
					centerPara.setAlignment(ParagraphAlignment.CENTER);
					XWPFRun centerRun = centerPara.createRun();
					centerRun.setBold(true);
					centerRun.setFontSize(11);
					centerRun.setText("Quiz Mania Ultimate Challenge");
					centerRun.addBreak();
					centerRun.setText("ITPEC - " + Sclass);
					centerRun.setBold(true);
					centerRun.addBreak();
					centerRun.setText("Batch - ??");
					centerRun.setBold(true);

					// === Right Cell: ITPEC Logo ===
					XWPFTableCell rightCell = headerRow.getCell(2);
					XWPFParagraph rightPara = rightCell.getParagraphs().get(0);
					rightPara.setAlignment(ParagraphAlignment.RIGHT);
					rightCell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000)); // 20% of table width
					rightCell.getCTTc().getTcPr().getTcW().setType(STTblWidth.PCT);
					XWPFRun rightRun = rightPara.createRun();
					try (InputStream itpecLogo = new FileInputStream(
							"D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images\\ITPEC-logo-transparent.png")) {
						rightRun.addPicture(itpecLogo, Document.PICTURE_TYPE_PNG, "itpec.png", Units.toEMU(80),
								Units.toEMU(60));
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					// Date and Time info
					XWPFParagraph datePara = document.createParagraph();
					datePara.setAlignment(ParagraphAlignment.BOTH);
					XWPFRun dateRun = datePara.createRun();
					dateRun.setFontSize(12);
					dateRun.setText("Date:(???) 							           Time allowed: ?? mins");

					// Add horizontal line
					XWPFParagraph space3 = document.createParagraph();
					XWPFRun spacei3 = space3.createRun();
					spacei3.setText(" ");

					// Instructions
					XWPFParagraph instructionPara = document.createParagraph();
					XWPFRun instructionRun = instructionPara.createRun();
					instructionRun.setFontSize(12);
					instructionRun.setText("Answer all questions. ( " + rowCount + " marks)");

					// Horizontal line (manual)
					XWPFParagraph linePara = document.createParagraph();
					XWPFRun lineRun = linePara.createRun();
					lineRun.setText(
							"_________________________________________________________________________________");

					// Add horizontal line
					XWPFParagraph space4 = document.createParagraph();
					XWPFRun spacei4 = space4.createRun();
					spacei4.setText(" ");

					// Section title
					XWPFParagraph sectionTitle = document.createParagraph();
					XWPFRun sectionRun = sectionTitle.createRun();
					sectionRun.setBold(true);
					sectionRun.setFontSize(12);
					sectionRun.setText("Multiple-choice questions.");

					// Add horizontal line
					XWPFParagraph space5 = document.createParagraph();
					XWPFRun spacei5 = space5.createRun();
					spacei5.setText(" ");

					// Process each question and answer
					List<String> correctAnswers = new ArrayList<>();
					for (int i = 0; i < limitedQuestions.size(); i++) {
						Question question = limitedQuestions.get(i);

						// Question Text
						XWPFParagraph questionPara = document.createParagraph();
						XWPFRun questionRun = questionPara.createRun();
						questionRun.setText("Q" + (i + 1) + ": " + question.getQ_desc());
						questionRun.setFontSize(12);
						questionRun.addBreak();

						// Add question image (if exists)
						if (question.getImg_id() != 0) {
							InputStream questionImageStream = fetchImageFromDB(question.getImg_id(), "q_image");
							if (questionImageStream != null) {
								try {
									addImageToDocx(document, questionImageStream);
								} catch (InvalidFormatException e1) {
									e1.printStackTrace();
								}
							}
						}

						// List answers (a, b, c, d)
						String[] answers = { question.getAns_a(), question.getAns_b(), question.getAns_c(), question.getAns_d() };
						for (int j = 0; j < answers.length; j++) {
							XWPFParagraph ansPara = document.createParagraph();
							XWPFRun ansRun = ansPara.createRun();
							ansRun.setText("(" + (char) ('a' + j) + ") " + answers[j]);
							ansRun.setFontSize(12);

							// Add answer image (if exists)
							InputStream answerImageStream = fetchImageFromDB(question.getImg_id(),
									"ans_img_" + (char) ('a' + j));
							if (answerImageStream != null) {
								try {
									addImageToDocx(document, answerImageStream);
								} catch (InvalidFormatException e1) {
									e1.printStackTrace();
								}
							}

							// Add vertical spacing
							XWPFParagraph space6 = document.createParagraph();
							XWPFRun spacei6 = space6.createRun();
							spacei6.setText(" ");
						}

						// Add horizontal line
						XWPFParagraph space7 = document.createParagraph();
						XWPFRun spacei7 = space7.createRun();
						spacei7.setText(" ");

						// Store correct answer
						char correctOption = ' ';
						String correctAnswer = question.getQ_ans();

						// First try exact match (case insensitive)
						for (int j = 0; j < answers.length; j++) {
						    if (correctAnswer != null && answers[j] != null) {
						        if (correctAnswer.equalsIgnoreCase(answers[j])) {
						            correctOption = (char) ('a' + j);
						            break;
						        }
						    }
						}

						// If no exact match, try normalized comparison
						if (correctOption == ' ') {
						    for (int j = 0; j < answers.length; j++) {
						        if (correctAnswer != null && answers[j] != null) {
						        	String cleanCorrect = correctAnswer.trim()
						                .replaceAll("\\s+", " ")
						                .toLowerCase()
						                .replaceAll("[^a-z0-9]", "");
						            
						            String cleanOption = answers[j].trim()
						                .replaceAll("\\s+", " ")
						                .toLowerCase()
						                .replaceAll("[^a-z0-9]", "");
						             
						            if (cleanCorrect.equals(cleanOption)) {
						                correctOption = (char) ('a' + j);
						                break;
						            }
						        }
						    }
						}

						// Final fallback - try contains match
						if (correctOption == ' ') {
						    for (int j = 0; j < answers.length; j++) {
						        if (correctAnswer != null && answers[j] != null) {
						            String cleanCorrect = correctAnswer.trim().toLowerCase();
						            String cleanOption = answers[j].trim().toLowerCase();
						            
						            if (cleanOption.contains(cleanCorrect) || cleanCorrect.contains(cleanOption)) {
						                correctOption = (char) ('a' + j);
						                break;
						            }
						        }
						    }
						}

						// Store result - use space if no match found
						correctAnswers.add(correctOption == ' ' ? " " : String.valueOf(correctOption));
						}
					// Insert a page break before the Correct Answers section
					XWPFParagraph pageBreakPara = document.createParagraph();
					XWPFRun pageBreakRun = pageBreakPara.createRun();
					pageBreakRun.addBreak(BreakType.PAGE);

					// Correct Answers Section Title
					XWPFParagraph ansTitle = document.createParagraph();
					XWPFRun ansTitleRun = ansTitle.createRun();
					ansTitleRun.setText("Correct Answers:");
					ansTitleRun.setBold(true);
					ansTitleRun.setFontSize(14);
					ansTitleRun.addBreak();

					// Prepare two columns: left = Q1 to Qn/2, right = Q(n/2)+1 to Qn
					int total = correctAnswers.size();
					int mid = (total + 1) / 2; // split point (round up for odd)

					XWPFTable table = document.createTable(); // Start with one row
					table.removeBorders(); // No visible borders

					for (int i = 0; i < mid; i++) {
					    XWPFTableRow row = (i == 0) ? table.getRow(0) : table.createRow();

					    // Left column: Q1 to Q(mid)
					    XWPFTableCell leftCell1 = row.getCell(0) != null ? row.getCell(0) : row.createCell();
					    XWPFParagraph leftPara1 = leftCell1.getParagraphs().get(0);
					    XWPFRun leftRun1 = leftPara1.createRun();
					    String leftAnswer = correctAnswers.get(i);
					    if (leftAnswer == null || leftAnswer.trim().isEmpty()) {
					        leftRun1.setText("Q" + (i + 1) + ": [ERROR]");
					        leftRun1.setColor("FF0000");
					    } else {
					        leftRun1.setText("Q" + (i + 1) + ": (" + leftAnswer + ")");
					    }
					    leftRun1.setFontSize(12);

					    // Right column: Q(mid+1) to Q(n)
					    int rightIndex = i + mid;
					    if (rightIndex < total) {
					        XWPFTableCell rightCell1 = (row.getTableCells().size() > 1) ? row.getCell(1) : row.createCell();
					        XWPFParagraph rightPara1 = rightCell1.getParagraphs().get(0);
					        XWPFRun rightRun1 = rightPara1.createRun();
					        String rightAnswer = correctAnswers.get(rightIndex);
					        if (rightAnswer == null || rightAnswer.trim().isEmpty()) {
					            rightRun1.setText("Q" + (rightIndex + 1) + ": [ERROR]");
					            rightRun1.setColor("FF0000");
					        } else {
					            rightRun1.setText("Q" + (rightIndex + 1) + ": (" + rightAnswer + ")");
					        }
					        rightRun1.setFontSize(12);
					    }
					}

					// Optional: Stretch the table to full width
					table.setWidth("100%");
					CTTblPr tblPr = table.getCTTbl().getTblPr();
					if (tblPr == null) tblPr = table.getCTTbl().addNewTblPr();
					CTTblWidth width = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
					width.setType(STTblWidth.DXA);
					width.setW(BigInteger.valueOf(9000));

					// Remove borders
					CTTblBorders borders = tblPr.isSetTblBorders() ? tblPr.getTblBorders() : tblPr.addNewTblBorders();
					borders.addNewTop().setVal(STBorder.NONE);
					borders.addNewBottom().setVal(STBorder.NONE);
					borders.addNewLeft().setVal(STBorder.NONE);
					borders.addNewRight().setVal(STBorder.NONE);
					borders.addNewInsideH().setVal(STBorder.NONE);
					borders.addNewInsideV().setVal(STBorder.NONE);

					// Add some spacing after the table
					document.createParagraph();

					// Create Footer with Page Number
					XWPFHeaderFooterPolicy headerPolicy = new XWPFHeaderFooterPolicy(document);
					XWPFFooter footer = headerPolicy.createFooter(STHdrFtr.DEFAULT);
					XWPFParagraph footerPara = footer.createParagraph();
					footerPara.setAlignment(ParagraphAlignment.RIGHT);

					XWPFRun footerRun = footerPara.createRun();
					footerRun.setText("Page ");

					CTSimpleField pageNumberField = footerPara.getCTP().addNewFldSimple();
					pageNumberField.setInstr("PAGE \\* MERGEFORMAT");
					pageNumberField.setDirty(true);

					// Save file
					try (FileOutputStream out = new FileOutputStream(file)) {
						document.write(out);
					}

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("DOCX Saved");
					alert.setHeaderText(null);
					alert.setContentText("DOCX file has been successfully saved:\n" + file.getAbsolutePath());
					alert.showAndWait();
				} catch (IOException | SQLException ex) {
					ex.printStackTrace();
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("An error occurred");
					alert.setContentText("Could not save DOCX. Please try again.");
					alert.showAndWait();
				}
			}
			stage.close();
		});

		// Cancel button action
		btnCancel.setOnAction(e -> {
			// Close the window or reset the current view
			Stage currentStage = (Stage) scrollPane.getScene().getWindow();
			currentStage.close();
		});

		return scrollPane;
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

		String[] answers = { question.getAns_a(), question.getAns_b(), question.getAns_c(), question.getAns_d() };
		// Store correct answer
		char correctOption = ' ';
		String correctAnswer = question.getQ_ans();

		// First try exact match (case insensitive)
		for (int j = 0; j < answers.length; j++) {
		    if (correctAnswer != null && answers[j] != null) {
		        if (correctAnswer.equalsIgnoreCase(answers[j])) {
		            correctOption = (char) ('A' + j);
		            break;
		        }
		    }
		}

		// If no exact match, try normalized comparison
		if (correctOption == ' ') {
		    for (int j = 0; j < answers.length; j++) {
		        if (correctAnswer != null && answers[j] != null) {
		        	String cleanCorrect = correctAnswer.trim()
		                .replaceAll("\\s+", " ")
		                .toLowerCase()
		                .replaceAll("[^a-z0-9]", "");
		            
		            String cleanOption = answers[j].trim()
		                .replaceAll("\\s+", " ")
		                .toLowerCase()
		                .replaceAll("[^a-z0-9]", "");
		             
		            if (cleanCorrect.equals(cleanOption)) {
		                correctOption = (char) ('A' + j);
		                break;
		            }
		        }
		    }
		}

		// Final fallback - try contains match
		if (correctOption == ' ') {
		    for (int j = 0; j < answers.length; j++) {
		        if (correctAnswer != null && answers[j] != null) {
		            String cleanCorrect = correctAnswer.trim().toLowerCase();
		            String cleanOption = answers[j].trim().toLowerCase();
		            
		            if (cleanOption.contains(cleanCorrect) || cleanCorrect.contains(cleanOption)) {
		                correctOption = (char) ('A' + j);
		                break;
		            }
		        }
		    }
		}

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
		String[] qanswers = { "answer_a", "answer_b", "answer_c", "answer_d" };
		String[] imgColumns = { "ans_img_a", "ans_img_b", "ans_img_c", "ans_img_d" };

		String textQuery = "SELECT * FROM ipquestion WHERE img_id = ?";
		PreparedStatement textStmt = con.prepareStatement(textQuery);
		textStmt.setInt(1, imgId);
		ResultSet textRS = textStmt.executeQuery();

		if (textRS.next()) {
			for (int i = 0; i < qanswers.length; i++) {
				String answerText = textRS.getString(qanswers[i]);
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

					Label rbAnswer = new Label(answerText);
					rbAnswer.setWrapText(true);
					rbAnswer.setMaxWidth(500);
					rbAnswer.setMinHeight(Region.USE_PREF_SIZE);
					rbAnswer.setFont(Font.font(16));

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
					// Store the correct option
					if (answerText.equals(question.getQ_ans())) {
						correctOption = optionLetter;
					}
				}
			}
		}
		// Show correct answer below the question
		Label lblCorrectAnswer = new Label("Correct Answer: (" + correctOption + ")");
		lblCorrectAnswer.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
		questionPane.getChildren().add(lblCorrectAnswer);
		return questionPane;
	}

	public void getQueryQuestion() throws SQLException {
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();

		for (int i = 0; i < chapterCheckId.size(); i++) { // Start at 0 and loop up to size - 1
			for (int j = 0; j < yearmonthCheckId.size(); j++) { // Start at 0 for yearmonthCheckId
				PreparedStatement pst = con
						.prepareStatement("SELECT * FROM ipquestion WHERE c_id = ? AND ym_id = ? AND esection_id = ?");

				pst.setInt(1, chapterCheckId.get(i)); // Valid indexing for chapterCheckId
				pst.setInt(2, yearmonthCheckId.get(j)); // Valid indexing for yearmonthCheckId
				pst.setInt(3, selectedEsectionId);
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

}
