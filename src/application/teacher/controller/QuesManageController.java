package application.teacher.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.main.DatabaseConnection;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import application.teacher.form.BeforeUpdateQuestionFormCopy;
import application.teacher.form.QuesFileGenerateForm;
import application.teacher.form.QuestionForm;
import application.teacher.form.ScrollPaneForm;
import application.teacher.form.TeacherHomeForm;

public class QuesManageController {
	@FXML
	private TextField addYear;

	@FXML
	private TextField addMonth;

	@FXML
	private TextField chapter;

	@FXML
	private TextField type;
	
    @FXML
    private TextField etype;

	@FXML
	private Label lblWarning;

	@FXML
	private VBox vboxAddQuestions;

	@FXML
	public void initialize() {
		// Ensure the layout is fully rendered before moving the label
		Platform.runLater(() -> shakeWarningInsideVBox());
	}

	private void shakeWarningInsideVBox() {
		// Disable clipping for the VBox
		vboxAddQuestions.setClip(null);

		double vboxHeight = vboxAddQuestions.getHeight(); // Get VBox height

		// Ensure the value is valid
		if (vboxHeight > 0) {
			// Start the label at the current position
			double currentTranslateY = lblWarning.getTranslateY();

			// Create a shaking effect using TranslateTransition
			TranslateTransition shakeTransition = new TranslateTransition(Duration.seconds(1), lblWarning);
			shakeTransition.setFromY(currentTranslateY); // Starting position (current position)
			shakeTransition.setByY(10); // Move 10 pixels down
			shakeTransition.setCycleCount(TranslateTransition.INDEFINITE); // Infinite shake
			shakeTransition.setAutoReverse(true); // Automatically reverse the direction after each move

			// Start the shake animation
			shakeTransition.play(); // Start the shake animation
		}
	}

	private boolean duplicateCheckYear() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("select count(*) from yearchoice where year=? ");
		pst.setString(1, addYear.getText());

		ResultSet rs = pst.executeQuery();
		int rowCount = 0;
		if (rs.next()) {
			rowCount = rs.getInt(1);
		}

		if (rowCount > 0) {
			return true;
		}

		return false;
	}

	private boolean duplicateCheckMonth() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("select count(*) from monthchoice where month=? ");
		pst.setString(1, addMonth.getText());

		ResultSet rs = pst.executeQuery();
		int rowCount = 0;
		if (rs.next()) {
			rowCount = rs.getInt(1);
		}

		if (rowCount > 0) {
			return true;
		}

		return false;
	}

	public static int getMID(String month) throws SQLException {
		// TODO Auto-generated method stub
		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("select m_id from monthchoice where month=? ");
		pst.setString(1, month);

		ResultSet rs = pst.executeQuery();
		int m_id = 0;
		if (rs.next()) {
			m_id = rs.getInt(1);
		}
		return m_id;
	}

	public static int getYID(String year) throws SQLException {
		// TODO Auto-generated method stub
		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("select y_id from yearchoice where year=? ");
		pst.setString(1, year);

		ResultSet rs = pst.executeQuery();
		int y_id = 0;
		if (rs.next()) {
			y_id = rs.getInt(1);
		}
		return y_id;

	}

	public static int getChapterID(String chapter) throws SQLException {		// nyi nyi update 
	    int c_id = -1; // Default value if chapter not found
	    DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();
	    // Use try-with-resources to auto-close resources
	    try ( // Fix method name if it's a typo
	         PreparedStatement pst = con.prepareStatement("SELECT c_id FROM chapter WHERE chapter_name = ?")) {
	        pst.setString(1, chapter);

	        try (ResultSet rs = pst.executeQuery()) {
	            if (rs.next()) {
	                c_id = rs.getInt(1);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Log the exception for debugging
	    }

	    return c_id;
	}


	public static int getTypeID(String type) throws SQLException { 	// nyi nyi update  
		// TODO Auto-generated method stub
		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("select t_id from qtype where category=? ");
		pst.setString(1, type);

		ResultSet rs = pst.executeQuery();
		int t_id = -1;
		if (rs.next()) {
			t_id = rs.getInt(1);
		}
		return t_id;

	}
	
	public static int getExamTypeID(String etype) throws SQLException { 	// nyi nyi update  
		// TODO Auto-generated method stub
		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("select exam_id from examtype where exam_name=? ");
		pst.setString(1, etype);

		ResultSet rs = pst.executeQuery();
		int t_id = -1;
		if (rs.next()) {
			t_id = rs.getInt(1);
		}
		return t_id;

	}
	
	private boolean duplicateCheckMonthYear() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("select count(*) from yearMonth where y_id=? and m_id=? ");
		pst.setInt(1, getYID(addYear.getText()));
		pst.setInt(2, getMID(addMonth.getText()));

		ResultSet rs = pst.executeQuery();
		int rowCount = 0;
		if (rs.next()) {
			rowCount = rs.getInt(1);
		}

		if (rowCount > 0) {
			return true;
		}

		return false;
	}
	
	private boolean duplicateCheckChaptye() throws SQLException, ClassNotFoundException {		//nyi nyi update
		// TODO Auto-generated method stub
		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("select count(*) from chaptye where t_id=? and c_id=? ");
		pst.setInt(1, getYID(type.getText()));
		pst.setInt(2, getMID(chapter.getText()));

		ResultSet rs = pst.executeQuery();
		int rowCount = 0;
		if (rs.next()) {
			rowCount = rs.getInt(1);
		}

		if (rowCount > 0) {
			return true;
		}

		return false;
	}


	@FXML
	void btnNext(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
	    try {
	        // Validate input fields
	        if (chapter.getText().trim().isEmpty() || type.getText().trim().isEmpty() || etype.getText().trim().isEmpty()) {
	            showWarningAlert("Fill Data", "Please Fill Data!", "Please enter all required data.");
	            return;
	        }

	        if (duplicateCheckChaptye()) {
	            showWarningAlert("Duplicate Data", "Duplicate Data!", "This data already exists!");
	            return;
	        }else{
	        	// Ensure exam type exists
		        int exam_id = getExamTypeID(etype.getText());
		        if (exam_id == -1) {
		            saveExamType();
		            exam_id = getExamTypeID(etype.getText());
		            if (exam_id == -1) {
		                showWarningAlert("Error", "Exam Type Error", "Failed to save or retrieve exam type.");
		                return;
		            }
		        }

		        // Get or create chapter
		        int c_id = getChapterID(chapter.getText());
		        if (c_id == -1) {
		            saveChapters();
		            c_id = getChapterID(chapter.getText());
		        }

		        // Get or create type
		        int t_id = getTypeID(type.getText());
		        if (t_id == -1) {
		            saveType();
		            t_id = getTypeID(type.getText());
		        }

		        // If both IDs are available, link them
		        if (c_id != -1 && t_id != -1 && exam_id != -1) {
		            chapterAndTypeAdd(c_id, t_id, exam_id);

		            Alert alert = new Alert(Alert.AlertType.INFORMATION);
		            alert.setTitle("Save Data");
		            alert.setHeaderText("Successfully Saved Data!");
		            alert.setContentText("Data has been saved successfully.");
		            alert.show();
		        } else {
		            showWarningAlert("Save Error", "ID Error", "Unable to get valid IDs for chapter or type.");
		        }

		        // Clear input fields
		        chapter.clear();
		        type.clear();
		        etype.clear();
	        }

	    } catch (NullPointerException ex) {
	        System.out.println("Null Pointer Exception caught: " + ex.getMessage());
	    }
	}

	
//	-------------------------------------------------------------------------------
	
	private void showWarningAlert(String title, String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}

	private void saveChapters() throws SQLException {

		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("insert into chapter(chapter_name)values" + "(?)");
		pst.setString(1, chapter.getText());
		pst.executeUpdate();
	}
	private void saveExamType() throws SQLException {

		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("insert into examtype(exam_name)values (?)");
		pst.setString(1, etype.getText());
		pst.executeUpdate();
	}

	private void saveType() throws SQLException {

		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("insert into qtype(category)values" + "(?)");
		pst.setString(1, type.getText());
		pst.executeUpdate();
	}

	public void chapterAndTypeAdd(int c_id, int t_id, int exam_id) throws SQLException, IOException {		 // nyinyi update

		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();

		PreparedStatement pst = con.prepareStatement("insert into chaptye (t_id,c_id, exam_id)values" + "(?,?,?)");
		pst.setInt(1, getTypeID(type.getText()));
		pst.setInt(2, getChapterID(chapter.getText()));
		pst.setInt(3, getExamTypeID(etype.getText()));
		pst.executeUpdate();

	}

	@FXML
	void btnYearAdd(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

		try {
			if (addYear.getText().equals("") || addMonth.getText().equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Fill Data");
				alert.setHeaderText("Please Fill Data!");
				alert.setContentText("Please enter your data.");
				alert.show();
			} else {

				if (duplicateCheckMonthYear()) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Duplicate Data");
					alert.setHeaderText("Duplicate Data!");
					alert.setContentText("This data already exists!");
					alert.show();
				} else {
					if (duplicateCheckMonth() && !duplicateCheckYear()) {
						yearAdd();
					}
					if (!duplicateCheckMonth() && duplicateCheckYear()) {
						monthAdd();
					}
					if (!duplicateCheckMonth() && !duplicateCheckYear()) {
						yearAdd();
						monthAdd();
					}

					yearMonthAdd();

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Save Data");
					alert.setHeaderText("Successfully Saved data!");
					alert.setContentText("Good");
					alert.show();
				}
				addYear.clear();
				addMonth.clear();

			}
		} catch (NullPointerException ex) {
			System.out.println("Not Null");
		}

	}

	public void yearMonthAdd() throws SQLException, IOException {

		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();

		PreparedStatement pst = con.prepareStatement("insert into yearMonth (y_id,m_id)values" + "(?,?)");
		pst.setInt(1, getYID(addYear.getText()));
		pst.setInt(2, getMID(addMonth.getText()));
		pst.executeUpdate();

	}

	public void yearAdd() throws SQLException, IOException {

		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("insert into yearchoice(year)values" + "(?)");
		pst.setString(1, addYear.getText());
		pst.executeUpdate();

	}

	public void monthAdd() throws SQLException, IOException {

		DatabaseConnection db=new DatabaseConnection();
		Connection con = db.getConnetion();

		PreparedStatement pst = con.prepareStatement("insert into monthchoice(month)values" + "(?)");
		pst.setString(1, addMonth.getText());
		pst.executeUpdate();

	}
	@FXML
	void updateQuesAction(ActionEvent event) throws IOException {
		new BeforeUpdateQuestionFormCopy().toggleForm();
	}

	@FXML
	void addQuesAction(ActionEvent event) throws IOException {
		QuestionForm q = new QuestionForm();
		q.questionForm().show();
	}

	@FXML
	void addScheduleAction(ActionEvent event) throws IOException {
		ScrollPaneForm sr = new ScrollPaneForm();
		sr.scrollPaneForm().show();
	}
	  @FXML
	    void chapterEndtest(ActionEvent event) throws IOException {
		  QuesFileGenerateForm qf = new QuesFileGenerateForm();
		  TeacherHomeForm.root.setCenter(qf.quesFileGenerateForm());
	    }
}
