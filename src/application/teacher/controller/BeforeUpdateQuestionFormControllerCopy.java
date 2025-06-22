package application.teacher.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import application.main.DatabaseConnection;
import application.teacher.Question;
import application.teacher.form.BeforeUpdateQuestionFormCopy;
import application.teacher.form.UpdateQuestionAllForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class BeforeUpdateQuestionFormControllerCopy implements Initializable {
	public static Question update_Question;
	private CheckBox selectedChapterCheckBox;
	private CheckBox selectedYearMonthCheckBox;
	private int selectedEsectionId = 0;


	@FXML
	private TextField qNo;

	@FXML
	private ScrollPane scrollPane, chScrollPane;

	@FXML
	private RadioButton rbAm, rbIp, rbPm;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (qNo == null || scrollPane == null || chScrollPane == null) {
			showAlert(Alert.AlertType.ERROR, "Initialization Error", "UI Component Missing",
					"Some UI components were not loaded properly. Please restart the application.");
			return;
		}

		try {
			populateYearMonthCheckBox();
			chapterCheckBox();
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Database Error", "Data Load Failed",
					"There was an issue retrieving data from the database.");
		}

		setupRadioButtons();
	}
	
	private void setupRadioButtons() {
		ToggleGroup sectionToggleGroup = new ToggleGroup();
		rbIp.setToggleGroup(sectionToggleGroup);
		rbAm.setToggleGroup(sectionToggleGroup);
		rbPm.setToggleGroup(sectionToggleGroup);

		rbIp.setOnAction(e -> {
			selectedEsectionId = 1;
			// IP has no ETypeId
		});

		rbAm.setOnAction(e -> {
			selectedEsectionId = 2;
		});

		rbPm.setOnAction(e -> {
			selectedEsectionId = 3;
		});
	}


	public void populateYearMonthCheckBox() throws SQLException {
		String sql = "SELECT ym.ym_id, yc.year, mc.month FROM yearmonth ym " +
				"INNER JOIN yearchoice yc ON ym.y_id = yc.y_id " +
				"INNER JOIN monthchoice mc ON ym.m_id = mc.m_id";
		Connection con = new DatabaseConnection().getConnetion();
		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();

		VBox vbox = new VBox(10);
		Label titleLabel = new Label("Select Quiz Set");
		titleLabel.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-font-family:System; -fx-padding:7 0 3 10;");
		vbox.getChildren().add(titleLabel);

		while (rs.next()) {
			int ymId = rs.getInt("ym_id");
			String year = rs.getString("year");
			String month = rs.getString("month");
			CheckBox checkBox = new CheckBox(year + " " + month + " Exam");
			checkBox.setId(String.valueOf(ymId));
			checkBox.setCursor(Cursor.HAND);
			checkBox.setStyle("-fx-padding:0 10;");
			checkBox.setOnAction(event -> {
				if (selectedYearMonthCheckBox != null) {
					selectedYearMonthCheckBox.setSelected(false);
				}
				selectedYearMonthCheckBox = checkBox;
			});
			vbox.getChildren().add(checkBox);
		}
		con.close();
		scrollPane.setContent(vbox);
		scrollPane.setFitToWidth(true);
	}

	public void chapterCheckBox() throws SQLException {
		String sql = "SELECT * FROM chapter";
		Connection con = new DatabaseConnection().getConnetion();
		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();

		VBox vbox = new VBox(10);
		Label titleLabel = new Label("Select Category");
		titleLabel.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-font-family:System; -fx-padding:7 0 3 10;");
		vbox.getChildren().add(titleLabel);

		while (rs.next()) {
			int chapterId = rs.getInt("c_id");
			String chapter = rs.getString("chapter_name");
			CheckBox checkBox = new CheckBox(chapter);
			checkBox.setId(String.valueOf(chapterId));
			checkBox.setCursor(Cursor.HAND);
			checkBox.setStyle("-fx-padding:0 10;");
			checkBox.setOnAction(event -> {
				if (selectedChapterCheckBox != null) {
					selectedChapterCheckBox.setSelected(false);
				}
				selectedChapterCheckBox = checkBox;
			});
			vbox.getChildren().add(checkBox);
		}
		con.close();
		chScrollPane.setContent(vbox);
		chScrollPane.setFitToWidth(true);
	}

	@FXML
	void forQuesUpdate(ActionEvent event) throws IOException, SQLException {
		String qNoText = qNo.getText().trim();
		
		if (qNoText.isEmpty()) {
			showAlert(Alert.AlertType.WARNING, "Input Required", "Missing Question Number",
					"Please enter a valid Question Number.");
			return;
		}

		int qNumber;
		try {
			qNumber = Integer.parseInt(qNoText);
			if (qNumber < 1 || qNumber > 100) {
				showAlert(Alert.AlertType.WARNING, "Invalid Input", "Out of Range",
						"Question Number must be between 1 and 100.");
				return;
			}
		} catch (NumberFormatException e) {
			showAlert(Alert.AlertType.ERROR, "Invalid Input", "Not a Number",
					"Please enter a valid numeric Question Number (1-100).");
			return;
		}

		if (selectedChapterCheckBox == null) {
			showAlert(Alert.AlertType.WARNING, "Selection Required", "No Category Selected",
					"Please select a category before proceeding.");
			return;
		}

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

		if (selectedYearMonthCheckBox == null) {
			showAlert(Alert.AlertType.WARNING, "Selection Required", "No Quiz Set Selected",
					"Please select a quiz set before proceeding.");
			return;
		}

		String selectedYearMonthText = selectedYearMonthCheckBox.getText();
		String selectedChapterText = selectedChapterCheckBox.getText();

		Connection con = new DatabaseConnection().getConnetion();
		PreparedStatement pst = con.prepareStatement(
				"SELECT * FROM ipquestion WHERE q_no=? AND c_id = ? AND ym_id = ? AND esection_id = ?");
		pst.setInt(1, qNumber);
		pst.setInt(2, Integer.parseInt(selectedChapterCheckBox.getId()));
		pst.setInt(3, Integer.parseInt(selectedYearMonthCheckBox.getId()));
		pst.setInt(4, selectedEsectionId);
		
		ResultSet rs = pst.executeQuery();

		if (!rs.next()) {
			showAlert(Alert.AlertType.INFORMATION, "No Question Found", "Question Not Found",
					"No question exists with the given details:\nYear-Month: " + selectedYearMonthText +
							"\nCategory: " + selectedChapterText);
			con.close();
			return;
		}

		do {
			int qNo = rs.getInt("q_no");
			int qid = rs.getInt("Qip_id");
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
				update_Question = new Question(qid, qNo, qDesc, ansA, ansB, ansC, ansD, qAns,
						ym_id, img_id, c_id, t_id,
						rs1.getBytes("q_image"), exam_id, esection_id,
						rs1.getBytes("ans_img_a"), rs1.getBytes("ans_img_b"),
						rs1.getBytes("ans_img_c"), rs1.getBytes("ans_img_d"),
						rs1.getBytes("ans_cor_img"));
			}
		} while (rs.next());

		con.close();
		BeforeUpdateQuestionFormCopy.buqform.close();
		new UpdateQuestionAllForm().updateQuestionForm().show();
	}

	private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
