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
import application.teacher.form.ScheduleAdd;
import application.teacher.form.ScrollPaneForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ScrollPaneController implements Initializable {

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

//		PreparedStatement pst=con.prepareStatement("select * from schedule");
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
	void addScheduleAction(ActionEvent event) throws IOException {
		ScrollPaneForm.pane.close();
		new ScheduleAdd().toggleScheduleForm();
	}
	@FXML
	void cancelAction(ActionEvent event) throws IOException {
		ScrollPaneForm.pane.close();
	}
}
