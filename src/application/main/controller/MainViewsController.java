package application.main.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import application.main.form.HomeForm;
import application.main.DatabaseConnection;
import application.main.MQuestion;
import application.main.AppMain;
import application.main.form.AboutOfAdminForm;
import application.main.form.AboutOfTeacherForm;
import application.main.form.StudentViewForm;
import application.student.form.StudentRegisterationForm;
import application.student.form.StudentSignInForm;

public class MainViewsController implements Initializable {

	@FXML
	private Label monthExamDate;

	@FXML
	private Label chapterType;

	@FXML
	private Label examNum2;

	@FXML
	private Label monthExamChapter;

	@FXML
	private Label examNum1;

	@FXML
	private Label chapterTestExamDate;
	
	@FXML
	private Hyperlink h1;

    @FXML
    private Hyperlink h2;

    @FXML
    private Hyperlink h3;

    @FXML
    private Hyperlink h4;

    @FXML
    private Hyperlink h5;

    @FXML
    private Hyperlink h6;
//    /////

	@FXML
	private TableView<MQuestion> question;
	@FXML
	private TableColumn<MQuestion, String> qDescription;

	@FXML
	private TableColumn<MQuestion, Integer> qno;

	@FXML
	private TableColumn<MQuestion, String> qClass;

	@FXML
	private TableColumn<MQuestion, String> qExamTime;

	@FXML
	private TableColumn<MQuestion, Date> qExamDate;

	@FXML
	private TableColumn<MQuestion, Integer> qNumber;

	@FXML
	private TableColumn<MQuestion, Integer> qBatch;

	@FXML
	private TableColumn<MQuestion, Integer> qMarks;
	@FXML
	private TableColumn<MQuestion, String> qChapter;

//    for schedule
	
	private void applyHoverEffect(Hyperlink link) {
	    // Base style (non-hover)
	    String defaultStyle = "-fx-text-fill: white; -fx-font-size: 18px; " +
	                          "-fx-underline: false; -fx-background-radius: 10; " +
	                          "-fx-border-radius: 10; -fx-border-color: white; " +
	                          "-fx-border-width: 2; -fx-background-color: transparent;";
	    // Hover style
	    String hoverStyle = "-fx-text-fill: black; -fx-font-size: 18px; " +
	                        "-fx-underline: false; -fx-background-color: #6699ff; " +
	                        "-fx-background-radius: 10; -fx-border-radius: 10; " +
	                        "-fx-border-color: white; -fx-border-width: 2;";

	    // Apply default initially
	    link.setStyle(defaultStyle);

	    // Hover effect
	    link.setOnMouseEntered(e -> link.setStyle(hoverStyle));
	    link.setOnMouseExited(e -> link.setStyle(defaultStyle));
	}
	
	private void applyHoverEffectH(Hyperlink link) {
	    // Base style (non-hover)
	    String defaultStyle = "-fx-text-fill: white; -fx-font-size: 18px; " +
	                          "-fx-underline: false; -fx-background-radius: 10; " +
	                          "-fx-border-radius: 10; -fx-border-color: white; " +
	                          "-fx-border-width: 2; -fx-background-color: transparent;";
	    // Hover style
	    String hoverStyle = "-fx-text-fill: white; -fx-font-size: 18px; " +
	                        "-fx-underline: false; -fx-background-color: #2e5ddf;; " +
	                        "-fx-background-radius: 10; -fx-border-radius: 10; " +
	                        "-fx-border-color: white; -fx-border-width: 2;";

	    // Apply default initially
	    link.setStyle(defaultStyle);

	    // Hover effect
	    link.setOnMouseEntered(e -> link.setStyle(hoverStyle));
	    link.setOnMouseExited(e -> link.setStyle(defaultStyle));
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			applyHoverEffect(h1);
			applyHoverEffect(h2);
			applyHoverEffect(h3);
			applyHoverEffect(h4);
			applyHoverEffectH(h5);
			applyHoverEffectH(h6);
			examList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void examList() throws SQLException {
		qno.setCellValueFactory(new PropertyValueFactory<>("qid"));
		qClass.setCellValueFactory(new PropertyValueFactory<>("clas"));
		qExamTime.setCellValueFactory(new PropertyValueFactory<>("etime"));
		qDescription.setCellValueFactory(new PropertyValueFactory<>("qdesc"));
		qChapter.setCellValueFactory(new PropertyValueFactory<>("qchapter"));
		qExamDate.setCellValueFactory(new PropertyValueFactory<>("edate"));
		qNumber.setCellValueFactory(new PropertyValueFactory<>("qnum"));
		qBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
		qMarks.setCellValueFactory(new PropertyValueFactory<>("qmark"));
		question.setItems(getStudentListAll());
	}
	private ObservableList<MQuestion> getStudentListAll() throws SQLException {

		ObservableList<MQuestion> list = FXCollections.observableArrayList();

		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst=con.prepareStatement("SELECT * FROM schedule WHERE exam_date >= CURDATE()");
		pst.executeQuery();
		ResultSet rs = pst.executeQuery();
		int i = 0;
		while (rs.next()) {
			i++;
			int id = i;
			String qBatch = rs.getString(2);
			String qClass = rs.getString(3);
			String qChapter = rs.getString(4);
			String qDescription = rs.getString(5);
			String qExamTime = rs.getString(6);
			String qExamDate = rs.getString(7);
			String qNumber = rs.getString(8);
			String qMarks = rs.getString(9);
			MQuestion s = new MQuestion(id, qClass, qExamTime, qExamDate, qNumber, qBatch, qMarks, qDescription,qChapter);
					
			list.add(s);
		}
		return list;
	}

//    schedule end
	
//	Home Page
	@FXML
	void btnhome(ActionEvent event) throws IOException {
		HomeForm hm = new HomeForm();
		AppMain.root.setCenter(hm.homesForm());
		StudentRegisterationForm.closeForm();
	}

//	Teacher Page
	@FXML
	void btnteacher(ActionEvent event) throws IOException {
		AboutOfTeacherForm form =new AboutOfTeacherForm();
		AppMain.root.setCenter(form.aboutOfTeacherForm());
		StudentRegisterationForm.closeForm();
	}

//	Student Page
	@FXML
	void btnstudent(ActionEvent event) throws IOException {
		StudentViewForm stForm=new StudentViewForm();
		AppMain.root.setCenter(stForm.studentViewForm());
		StudentRegisterationForm.closeForm();
	}

//	Admin Page
	@FXML
	void btnadmin(ActionEvent event) throws IOException {
		AboutOfAdminForm aform=new AboutOfAdminForm();
		AppMain.root.setCenter(aform.aboutOfAdminForm());
		StudentRegisterationForm.closeForm();
	}
	
	@FXML
	void linkRegister(ActionEvent event) throws IOException {
	    StudentSignInForm.closeForm();
	    StudentRegisterationForm.closeForm();

	    new StudentRegisterationForm().toggleRegistrationForm();
	}

	@FXML
	void linkLogIn(ActionEvent event) throws IOException {
	    StudentRegisterationForm.closeForm();
	    StudentSignInForm.closeForm();

		new StudentSignInForm().toggleStudentSignInForm();
	}


}
