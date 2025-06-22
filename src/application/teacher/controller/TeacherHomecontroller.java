package application.teacher.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.main.DatabaseConnection;
import application.main.MQuestion;
import application.teacher.form.BeforeUpdateQuestionFormCopy;
import application.teacher.form.CheckQuestionFeForm;
import application.teacher.form.CheckQuestionIpForm;
import application.teacher.form.ContactUsForm;
import application.teacher.form.HomeForm;
import application.teacher.form.LogOutForm;
import application.teacher.form.ProfileForm;
import application.teacher.form.QuesManageForm;
import application.teacher.form.QuestionForm;
import application.teacher.form.StudentResultsFEForm;
import application.teacher.form.StudentResultsIPForm;
import application.teacher.form.TeacherHomeForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TeacherHomecontroller implements Initializable{
	
	 @FXML
	    private Hyperlink h1;

	    @FXML
	    private Hyperlink h10;

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

	    @FXML
	    private Hyperlink h7;

	    @FXML
	    private Hyperlink h8;

	    @FXML
	    private Hyperlink h9;
	    @FXML
	    private Hyperlink h11;
	    
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

	    
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			try {
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

//				PreparedStatement pst=con.prepareStatement("select * from schedule");
			PreparedStatement pst = con.prepareStatement("SELECT * FROM schedule WHERE exam_date >= CURDATE()");
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

				MQuestion s = new MQuestion(id, qClass, qExamTime, qExamDate, qNumber, qBatch, qMarks, qDescription,
						qChapter);

				list.add(s);
			}
			return list;
		}
	    

	@FXML
	void btnhomeAction(ActionEvent event) throws IOException {
		HomeForm home = new HomeForm();
		TeacherHomeForm.root.setCenter(home.homeForm());
	}

	@FXML
	void ipCheckQuestionLinkAction(ActionEvent event) throws IOException {
		CheckQuestionIpForm ck = new CheckQuestionIpForm();
		TeacherHomeForm.root.setCenter(ck.checkQuestionForm());
	}
	
	@FXML
	void feCheckQuestionLinkAction(ActionEvent event) throws IOException {
		CheckQuestionFeForm ck = new CheckQuestionFeForm();
		TeacherHomeForm.root.setCenter(ck.checkQuestionForm());
	}

	@FXML
	void questionManageLinkAction(ActionEvent event) throws IOException {
		QuesManageForm qm = new QuesManageForm();
		TeacherHomeForm.root.setCenter(qm.quesManageForm());
	}

	@FXML
	void ipResultAction(ActionEvent event) throws IOException {
		StudentResultsIPForm ip = new StudentResultsIPForm();
		TeacherHomeForm.root.setCenter(ip.studentResultsIPForm());
	}
	
	@FXML
	void feResultAction(ActionEvent event) throws IOException {
		StudentResultsFEForm fe = new StudentResultsFEForm();
		TeacherHomeForm.root.setCenter(fe.studentResultsFEForm());
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
	void updateQuestionLinkAction(ActionEvent event) throws IOException {
		QuesManageForm qm = new QuesManageForm();
		TeacherHomeForm.root.setCenter(qm.quesManageForm());
	}
	@FXML
    void contactUs(ActionEvent event) throws IOException {
		ContactUsForm cu = new ContactUsForm();
		TeacherHomeForm.root.setCenter(cu.contactUsForm());
    }

	@FXML
	void logoutLinkAction(ActionEvent event) throws IOException {
		LogOutForm logOutForm = new LogOutForm();
		logOutForm.toggleLogOutForm(); // Toggle Logout Form
		if (ProfileForm.profileStage != null && ProfileForm.profileStage.isShowing()) {
			ProfileForm.profileStage.close();
			ProfileForm.profileStage = null;
		}
	}

	@FXML
	void profileLinkAction(ActionEvent event) throws IOException {
		new ProfileForm().toggleProfileForm(); // Toggle Profile Form
		if (LogOutForm.logOutStage != null && LogOutForm.logOutStage.isShowing()) {
			LogOutForm.logOutStage.close();
			LogOutForm.logOutStage = null;
		}
	}
}
