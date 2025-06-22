package application.student.controller;

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
import application.student.form.Annou1Form;
import application.student.form.Annou2Form;
import application.student.form.Annou3Form;
import application.student.form.Annou4Form;
import application.student.form.Annou5Form;
import application.student.form.Annou6Form;
import application.student.form.BlogForm;
import application.student.form.ContactUsForm;
import application.student.form.ExamForm;
import application.student.form.HistoryForm;
import application.student.form.LogOutForm;
import application.student.form.MaintenanceForm;
import application.student.form.ProfileForm;
import application.student.form.StuDashboardForm;
import application.student.form.StudentHomeForm;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class StuDashboardController implements Initializable {

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

	@FXML
	private VBox annouBox1;

	@FXML
	private VBox annouBox2;

	@FXML
	private VBox annouBox3;

	@FXML
	private VBox annouBox4;

	@FXML
	private VBox annouBox5;

	@FXML
	private VBox annouBox6;

	private double waveOffset = 0.0;

	@FXML
    private Hyperlink blogLink;

    @FXML
    private Hyperlink contactUsLink;

    @FXML
    private Hyperlink examLink;

    @FXML
    private Hyperlink historyLink;

    @FXML
    private Hyperlink homeLink;
    
    @FXML
    private Hyperlink logoutLink;

    @FXML
    private Hyperlink profileLink;
	
	private void startWaveAnimation() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
			waveOffset += 0.09; // Increment wave phase

			// Apply different phase shifts for a natural wave effect
			annouBox6.setTranslateY(Math.sin(waveOffset) * 10); // Original wave
			annouBox5.setTranslateY(Math.sin(waveOffset + 1) * 10); // Slight delay
			annouBox4.setTranslateY(Math.sin(waveOffset + 2) * 10); // More delay
			annouBox3.setTranslateY(Math.sin(waveOffset + 3) * 10);
			annouBox2.setTranslateY(Math.sin(waveOffset + 4) * 10);
			annouBox1.setTranslateY(Math.sin(waveOffset + 5) * 10);
		}));

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}
	
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
			applyHoverEffect(homeLink);
		    applyHoverEffect(examLink);
		    applyHoverEffect(historyLink);
		    applyHoverEffect(blogLink);
		    applyHoverEffect(contactUsLink);
		    applyHoverEffectH(profileLink);
		    applyHoverEffectH(logoutLink);
			startWaveAnimation();
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

//			PreparedStatement pst=con.prepareStatement("select * from schedule");
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
	void logoutLinkAction(ActionEvent event) throws IOException {
		LogOutForm logOutForm = new LogOutForm();
		logOutForm.toggleLogOutForm(); // Toggle Logout Form
		if (ProfileForm.profileStage != null && ProfileForm.profileStage.isShowing()) {
			ProfileForm.profileStage.close();
			ProfileForm.profileStage = null;
		}
	}

	@FXML
	void updateProfileLinkAction(ActionEvent event) throws IOException {
		ProfileForm pform = new ProfileForm();
		pform.toggleProfileForm(); // Toggle Profile Form
		if (LogOutForm.logOutStage != null && LogOutForm.logOutStage.isShowing()) {
			LogOutForm.logOutStage.close();
			LogOutForm.logOutStage = null;
		}
	}

	@FXML
	void blogLinkAction(ActionEvent event) throws IOException {
	    BlogForm b = new BlogForm();
	    AnchorPane blogContent = b.blogForm();
	    applyZoomInEffect(blogContent); // Apply zoom-in effect
	    StuDashboardForm.root.setCenter(blogContent);
	}

	@FXML
	void contactUsLinkAction(ActionEvent event) throws IOException {
	    ContactUsForm conUs = new ContactUsForm();
	    AnchorPane contactUsContent = conUs.contactUsForm();
	    applyZoomInEffect(contactUsContent); // Apply zoom-in effect
	    StuDashboardForm.root.setCenter(contactUsContent);
	}

	@FXML
	void examLinkAction(ActionEvent event) throws IOException {
	    ExamForm em = new ExamForm();
	    AnchorPane examContent = em.examForm();
	    applyZoomInEffect(examContent); // Apply zoom-in effect
	    StuDashboardForm.root.setCenter(examContent);
	}

	@FXML
	void historyLinkAction(ActionEvent event) throws IOException, SQLException {
	    HistoryForm his = new HistoryForm();
	    AnchorPane historyContent = his.historyForm();
	    applyZoomInEffect(historyContent); // Apply zoom-in effect
	    StuDashboardForm.root.setCenter(historyContent);
	}

	@FXML
	void homeLinkAction(ActionEvent event) throws IOException {
	    StudentHomeForm home = new StudentHomeForm();
	    AnchorPane homeContent = home.homeForm();
	    applyZoomInEffect(homeContent); // Apply zoom-in effect
	    StuDashboardForm.root.setCenter(homeContent);
	}

	@FXML
	void leaderBoardLinkAction(ActionEvent event) throws IOException {
	    MaintenanceForm mt = new MaintenanceForm();
	    AnchorPane maintenanceContent = mt.maintenanceForm();
	    applyZoomInEffect(maintenanceContent); // Apply zoom-in effect
	    StuDashboardForm.root.setCenter(maintenanceContent);
	}

	private void applySlideInEffect(AnchorPane content) {
	    // Initially position the content off-screen to the right
	    content.setTranslateX(300);
	    
	    // Create a TranslateTransition to animate the slide-in effect
	    TranslateTransition slideTransition = new TranslateTransition(Duration.millis(300), content);
	    slideTransition.setFromX(300); // Start from off-screen
	    slideTransition.setToX(0); // Move to normal position
	    
	    // Play the animation
	    slideTransition.play();
	}

	@FXML
	void annou1LinkAction(ActionEvent event) throws IOException {
	    Annou1Form a1 = new Annou1Form();
	    AnchorPane annou1Content = a1.annou1Form();
	    applySlideInEffect(annou1Content);
	    StuDashboardForm.root.setCenter(annou1Content);
	}

	@FXML
	void annou2LinkAction(ActionEvent event) throws IOException {
	    Annou2Form a2 = new Annou2Form();
	    AnchorPane annou2Content = a2.annou2Form();
	    applySlideInEffect(annou2Content);
	    StuDashboardForm.root.setCenter(annou2Content);
	}

	@FXML
	void annou3LinkAction(ActionEvent event) throws IOException {
	    Annou3Form a3 = new Annou3Form();
	    AnchorPane annou3Content = a3.annou3Form();
	    applySlideInEffect(annou3Content);
	    StuDashboardForm.root.setCenter(annou3Content);
	}

	@FXML
	void annou4LinkAction(ActionEvent event) throws IOException {
	    Annou4Form a4 = new Annou4Form();
	    AnchorPane annou4Content = a4.annou4Form();
	    applySlideInEffect(annou4Content);
	    StuDashboardForm.root.setCenter(annou4Content);
	}

	@FXML
	void annou5LinkAction(ActionEvent event) throws IOException {
	    Annou5Form a5 = new Annou5Form();
	    AnchorPane annou5Content = a5.annou5Form();
	    applySlideInEffect(annou5Content);
	    StuDashboardForm.root.setCenter(annou5Content);
	}

	@FXML
	void annou6LinkAction(ActionEvent event) throws IOException {
	    Annou6Form a6 = new Annou6Form();
	    AnchorPane annou6Content = a6.annou6Form();
	    applySlideInEffect(annou6Content);
	    StuDashboardForm.root.setCenter(annou6Content);
	}

	private void applyZoomInEffect(AnchorPane content) {
	    // Initially scale the content down to 0.5 (zoomed out)
	    content.setScaleX(0.5);
	    content.setScaleY(0.5);

	    // Create a ScaleTransition to animate the zoom-in effect
	    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), content);
	    scaleTransition.setFromX(0.5);  // Start scale (zoomed out)
	    scaleTransition.setFromY(0.5);  // Start scale (zoomed out)
	    scaleTransition.setToX(1.0);    // End scale (normal size)
	    scaleTransition.setToY(1.0);    // End scale (normal size)

	    // Play the animation
	    scaleTransition.play();
	}
}
