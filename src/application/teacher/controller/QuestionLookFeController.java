package application.teacher.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.main.DatabaseConnection;
import application.teacher.Question;
import application.teacher.form.QuestionLookFeForm;
import application.teacher.form.UpdateQuestionFeForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class QuestionLookFeController implements Initializable {
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
	int tid = -1;
	int ttid = -1;
	@FXML
	private List<File> answerImages = new ArrayList<>(); // List to store selected answer images
	@FXML
	private List<String> answerText = new ArrayList<>();
	public String section;
	Question updQues = CheckQuestionFeController.check_Question;

	@FXML
	void btnquestionUpdate(ActionEvent event) {
		try {
			QuestionLookFeForm.form.close();
			UpdateQuestionFeForm up = new UpdateQuestionFeForm();
			up.updateQuestionForm2().show();
		} catch (Exception e) {
			e.printStackTrace();
			showAlert("Error", "Failed to add question", "An error occurred while adding the question.");
		}
	}
	private void showAlert(String title, String header, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
				setImage(qimage, updQues.getQ_descimg());
				setImage(aImage, updQues.getAns_aimg());
				setImage(bImage, updQues.getAns_bimg());
				setImage(cImage, updQues.getAns_cimg());
				setImage(dImage, updQues.getAns_dimg());
				setImage(ansImage, updQues.getQ_ansimg());

				System.out.println("question No: "+updQues.getQNo());
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Error: updQues is null.");
		}
	}

	private void setImage(ImageView imageView, byte[] imageData) {
		if (imageData != null) {
			imageView.setImage(new Image(new ByteArrayInputStream(imageData)));
		} else {
			System.out.println("Error: Image data is null.");
		}
	}
	@FXML
	void btnCancel(ActionEvent event) {
		QuestionLookFeForm.form.close();
	}

}
