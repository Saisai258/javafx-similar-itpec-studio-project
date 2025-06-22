package application.student.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.main.DatabaseConnection;
import application.student.form.FeAmChForm;
import application.student.form.StuDashboardForm;
import application.student.form.StudentHomeForm;
import application.teacher.Question;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FEResultController {

	private int totalQuestions = FeAmChController.limitedQuestions.size();
	private List<Question> reviewQuestions;
	private int[] reviewAnswers;
	private int reviewTotalQuestions;

	// Method to receive data from FeAmChController
	public void setReviewData(List<Question> questions, int[] answers, int totalQuestions) {
		this.reviewQuestions = questions;
		this.reviewAnswers = answers;
		this.setReviewTotalQuestions(totalQuestions);
	}

	@FXML
	public BorderPane getResultView() {
		// Top: Title Bar
		VBox topVBox = new VBox();
		topVBox.setAlignment(Pos.TOP_RIGHT);
		topVBox.setPrefHeight(100);
		topVBox.setPrefWidth(1000);
		topVBox.setStyle("-fx-background-color: linear-gradient(to left, #3377ff ,  #e6f7ff);");

		Label titleLabel = new Label("IP Test Result");
		titleLabel.setPrefHeight(100);
		titleLabel.setPrefWidth(351);
		titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
		titleLabel.setFont(new javafx.scene.text.Font("Monotype Corsiva", 35));
		topVBox.getChildren().add(titleLabel);

		// Left and Right: Decorative VBox
		VBox leftVBox = new VBox();
		leftVBox.setPrefHeight(450);
		leftVBox.setPrefWidth(100);
		leftVBox.setStyle("-fx-background-color: linear-gradient(to bottom , #3377ff ,  #e6f7ff);");

		VBox rightVBox = new VBox();
		rightVBox.setPrefHeight(450);
		rightVBox.setPrefWidth(100);
		rightVBox.setStyle("-fx-background-color: linear-gradient(to top, #3377ff ,  #e6f7ff);");

		// Center: PieChart with Result
		HBox centerHBox = new HBox();
		centerHBox.setAlignment(Pos.TOP_CENTER);
		centerHBox.setPrefHeight(100);
		centerHBox.setPrefWidth(200);

		// Create PieChart
		PieChart pieChart = new PieChart();
		int incorrectOrUnanswered = totalQuestions - FeAmChController.examTotalMark;
		PieChart.Data correctData = new PieChart.Data("Correct Answers", FeAmChController.examTotalMark);
		PieChart.Data incorrectData = new PieChart.Data("Incorrect/Unanswered", incorrectOrUnanswered);

		pieChart.getData().addAll(correctData, incorrectData);

		Label centerLabel = new Label();
		centerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: white;");
		double percentage = ((double) FeAmChController.examTotalMark / totalQuestions) * 100;
		centerLabel.setText(String.format("%.1f%%", percentage));

		StackPane pieContainer = new StackPane(pieChart, centerLabel);
		pieContainer.setAlignment(Pos.CENTER);

		Platform.runLater(() -> {
			correctData.getNode().setStyle("-fx-pie-color: green;");
			incorrectData.getNode().setStyle("-fx-pie-color: red;");
			updateLegendColors(pieChart);
		});

		Label resultLabel = new Label();
		resultLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-padding: 10;");
		if (FeAmChController.examTotalMark >= totalQuestions * 0.6) {
			resultLabel.setText("Successful");
			resultLabel.setStyle(resultLabel.getStyle() + "-fx-text-fill: green;");
		} else {
			resultLabel.setText("Failed");
			resultLabel.setStyle(resultLabel.getStyle() + "-fx-text-fill: red;");
		}

		// Label for additional message
		Label messageLabel = new Label();
		messageLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: black; -fx-padding: 10;");
		if (FeAmChController.examTotalMark >= totalQuestions * 0.6) {
			messageLabel.setText("Congratulations! You did well!");
		} else {
			messageLabel.setText("Better luck next time! Keep practicing.");
		}

		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(resultLabel, messageLabel);

		centerHBox.getChildren().addAll(pieContainer, vbox);

		// Bottom: Navigation Buttons
		HBox bottomHBox = new HBox();
		bottomHBox.setAlignment(Pos.CENTER_RIGHT);
		bottomHBox.setPrefHeight(100);
		bottomHBox.setPrefWidth(200);
		bottomHBox.setStyle("-fx-background-color: linear-gradient(to right, #3377ff ,  #e6f7ff);");

		Button startAgainButton = new Button("Start Again");
		startAgainButton.setPrefHeight(40);
		startAgainButton.setPrefWidth(140);
		startAgainButton.setCursor(Cursor.HAND);
		startAgainButton.setStyle("-fx-background-color: gold; -fx-background-radius: 10;");
		startAgainButton.setFont(new javafx.scene.text.Font("Times New Roman", 20));
		startAgainButton.setOnAction(arg0 -> {
			try {
				startAgainLinkAction(arg0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		HBox.setMargin(startAgainButton, new Insets(0, 0, 0, 20));

		Button homeButton = new Button("Go Home");
		homeButton.setPrefHeight(40);
		homeButton.setPrefWidth(140);
		homeButton.setCursor(Cursor.HAND);
		homeButton.setStyle("-fx-background-color: gold; -fx-background-radius: 10;");
		homeButton.setFont(new javafx.scene.text.Font("Times New Roman", 20));
		homeButton.setOnAction(arg0 -> {
			try {
				homeLinkAction(arg0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		HBox.setMargin(homeButton, new Insets(0, 20, 0, 20));
		
		// Add Review Answers button
				Button reviewButton = new Button("Review");
				reviewButton.setPrefHeight(40);
				reviewButton.setPrefWidth(140);
				reviewButton.setCursor(Cursor.HAND);
				reviewButton.setStyle("-fx-background-color: gold; -fx-background-radius: 10;");
				reviewButton.setFont(new javafx.scene.text.Font("Times New Roman", 20));

				reviewButton.setOnAction(e -> {
					Stage reviewStage = new Stage();
					ScrollPane reviewPane = new ScrollPane();
					try {
						reviewPane = showReviewPane();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Scene reviewScene = new Scene(reviewPane, 1366, 700);
					reviewStage.setTitle("Quiz Mania Ultimate Challenge");
					reviewStage.getIcons().add(new Image("downLogo.jpg"));
					reviewStage.setScene(reviewScene);
					reviewStage.show();
				});

		bottomHBox.getChildren().addAll(reviewButton,startAgainButton, homeButton);

		// Add to BorderPane
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topVBox);
		borderPane.setLeft(leftVBox);
		borderPane.setRight(rightVBox);
		borderPane.setCenter(centerHBox);
		borderPane.setBottom(bottomHBox);
		return borderPane;
	}

	private ScrollPane showReviewPane() throws SQLException {
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
		Label headerLabel = new Label("Review Your Answers");
		headerLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: navy;");
		headerLabel.setAlignment(Pos.CENTER);
		mainVBox.getChildren().add(headerLabel);

		VBox vboxQuestion = new VBox();
		vboxQuestion.setSpacing(10);
		vboxQuestion.setPrefWidth(1366);
		vboxQuestion.setAlignment(Pos.TOP_CENTER);

		for (int i = 0; i < reviewQuestions.size(); i++) {
			VBox questionPane = createReviewQuestionPane(reviewQuestions.get(i), reviewAnswers[i], i + 1, i // question
																											// index
			);
			vboxQuestion.getChildren().add(questionPane);
		}

		mainVBox.getChildren().add(vboxQuestion);

		// Create and style the Close button
		Button btnClose = new Button("Close");
		btnClose.setCursor(Cursor.HAND);
		btnClose.setStyle(
				"-fx-background-color: #3377ff; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px;");

		HBox buttonBox = new HBox(20);
		buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
		buttonBox.setPadding(new Insets(50));
		buttonBox.getChildren().add(btnClose);
		mainVBox.getChildren().add(buttonBox);

		btnClose.setOnAction(e -> {
			Stage currentStage = (Stage) scrollPane.getScene().getWindow();
			currentStage.close();
		});

		return scrollPane;
	}

	public VBox createReviewQuestionPane(Question question, int answerStatus, int qNo, int questionIndex)
        throws SQLException {
    VBox questionPane = new VBox(10);
    questionPane.setMaxWidth(900);
    questionPane.setPadding(new Insets(20));
    questionPane.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10;");

    // Question status header
    HBox statusHeader = new HBox(10);
    statusHeader.setAlignment(Pos.CENTER_LEFT);

    String statusSymbol = "";
    String statusColor = "";
    String userAnswer = getUserAnswer(question, answerStatus, questionIndex);
    if (userAnswer == null) {
        statusSymbol = "🚫";
        statusColor = "-fx-text-fill: red;";
        answerStatus = -1; // Mark as unanswered
    } else if (answerStatus == 1) {
        statusSymbol = "✓";
        statusColor = "-fx-text-fill: green;";
    } else {
        statusSymbol = "✗";
        statusColor = "-fx-text-fill: red;";
    }

    Label statusLabel = new Label(statusSymbol + " Q" + qNo + ": ");
    statusLabel.setWrapText(true);
    statusLabel.setStyle("-fx-font-size: 16px; " + statusColor);
    statusLabel.setMinWidth(50);
    statusLabel.setMinHeight(Region.USE_PREF_SIZE);
    statusHeader.getChildren().add(statusLabel);
    questionPane.getChildren().add(statusHeader);

    // Question description
    Label txtQDesp = new Label(question.getQ_desc());
    txtQDesp.setWrapText(true);
    txtQDesp.setStyle("-fx-font-size: 16px; -fx-padding: 0 0 10 20;");
    txtQDesp.setMaxWidth(Double.MAX_VALUE);
    txtQDesp.setMinHeight(Region.USE_PREF_SIZE);
    VBox.setVgrow(txtQDesp, Priority.ALWAYS);
    questionPane.getChildren().add(txtQDesp);

    // Load question image if exists
    ImageView questionImage = loadQuestionImage(question.getImg_id());
    if (questionImage != null) {
        questionPane.getChildren().add(questionImage);
    }

    String[] answers = { "answer_a", "answer_b", "answer_c", "answer_d" };
    String[] answerImages = { "ans_img_a", "ans_img_b", "ans_img_c", "ans_img_d" };
    String correctAnswer = question.getQ_ans();

    DatabaseConnection db = new DatabaseConnection();
    Connection con = db.getConnetion();

    // First get the image data if it exists
    Map<String, ImageView> answerImageViews = new HashMap<>();
    try (PreparedStatement imgStmt = con.prepareStatement("SELECT * FROM questionImage WHERE img_id = ?")) {
        imgStmt.setInt(1, question.getImg_id());
        ResultSet imgRs = imgStmt.executeQuery();
        
        if (imgRs.next()) {
            for (int i = 0; i < answerImages.length; i++) {
                InputStream imgStream = imgRs.getBinaryStream(answerImages[i]);
                if (imgStream != null) {
                    Image image = new Image(imgStream);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(150);
                    imageView.setPreserveRatio(true);
                    answerImageViews.put(answerImages[i], imageView);
                }
            }
        }
    }

    // Then get the answer text data
    try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM ipquestion WHERE Qip_id = ?")) {
        stmt.setInt(1, question.getQ_id());
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            for (int i = 0; i < answers.length; i++) {
                String answerText = rs.getString(answers[i]);
                if (answerText != null || answerImageViews.containsKey(answerImages[i])) {
                    VBox answerBox = new VBox(5); // Use VBox to stack text and image vertically
                    answerBox.setAlignment(Pos.CENTER_LEFT);
                    answerBox.setStyle("-fx-padding: 5 0 5 20;");

                    HBox answerTextRow = new HBox(10);
                    answerTextRow.setAlignment(Pos.CENTER_LEFT);

                    char optionLetter = (char) ('A' + i);
                    Label optionLabel = new Label(optionLetter + ".");
                    optionLabel.setStyle("-fx-font-size: 16px; -fx-min-width: 50;");
                    optionLabel.setMinHeight(Region.USE_PREF_SIZE);

                    Label answerLabel = new Label(answerText != null ? answerText : "");
                    answerLabel.setStyle("-fx-font-size: 16px;");
                    answerLabel.setMaxWidth(500);
                    answerLabel.setMinHeight(Region.USE_PREF_SIZE);
                    answerLabel.setWrapText(true);

                    // Add status indicators
                    if (answerText == null || answerText.isEmpty()) {
                        answerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red; -fx-font-size: 16px;");
                        answerTextRow.getChildren().add(new Label("🚫 "));
                    } else if (normalize(answerText).equals(normalize(correctAnswer))) {
                        answerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green; -fx-font-size: 16px;");
                        answerTextRow.getChildren().add(new Label("✓ "));
                    } else if (normalize(answerText).equals(normalize(userAnswer))) {
                        answerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red; -fx-font-size: 16px;");
                        answerTextRow.getChildren().add(new Label("✗ "));
                    }

                    answerTextRow.getChildren().addAll(optionLabel, answerLabel);
                    answerBox.getChildren().add(answerTextRow);

                    // Add answer image if available
                    ImageView answerImageView = answerImageViews.get(answerImages[i]);
                    if (answerImageView != null) {
                        answerBox.getChildren().add(answerImageView);
                    }

                    questionPane.getChildren().add(answerBox);
                }
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

	private ImageView loadQuestionImage(int imgId) throws SQLException {
		if (imgId == 0)
			return null;

		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		try (PreparedStatement stmt = con.prepareStatement("SELECT q_image FROM questionImage WHERE img_id = ?")) {
			stmt.setInt(1, imgId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				InputStream is = rs.getBinaryStream("q_image");
				if (is != null) {
					Image image = new Image(is);
					ImageView imageView = new ImageView(image);
					imageView.setFitHeight(200);
					imageView.setPreserveRatio(true);
					return imageView;
				}
			}
		}
		return null;
	}

	private String getUserAnswer(Question question, int answerStatus, int questionIndex) {
		// Get the stored user answers from FeAmChController
		Map<Integer, String> userSelections = FeAmChController.userAnswerSelections;

		if (answerStatus == -1) {
			return null; // No answer
		}

		// Return the user's selected answer text
		return userSelections.getOrDefault(questionIndex, null);
	}

	private void updateLegendColors(PieChart pieChart) {
		for (Node node : pieChart.lookupAll(".chart-legend-item")) {
			if (node instanceof Label) {
				Label label = (Label) node;
				for (PieChart.Data data : pieChart.getData()) {
					if (label.getText().equals(data.getName())) {
						Node symbol = label.getGraphic();
						if (symbol != null) {
							if (data.getName().equals("Correct Answers")) {
								symbol.setStyle("-fx-background-color: green;"); // Green for correct
							} else if (data.getName().equals("Incorrect/Unanswered")) {
								symbol.setStyle("-fx-background-color: red;"); // Red for incorrect
							}
						}
					}
				}
			}
		}
	}

	@FXML
	void homeLinkAction(ActionEvent event) throws IOException {
		FeAmChController.examTotalMark = 0;
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		currentStage.close();
		StudentHomeForm home = new StudentHomeForm();
		AnchorPane homeContent = home.homeForm();
		applyZoomInFadeInEffect(homeContent); // Apply zoom-in effect
		StuDashboardForm.root.setCenter(homeContent);
	}

	@FXML
	void startAgainLinkAction(ActionEvent event) throws IOException {
		FeAmChController.examTotalMark = 0;
		// Get the current stage (window) and close it
		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		currentStage.close();
		FeAmChForm ip = new FeAmChForm();
		AnchorPane examContent = ip.feAmChExamForm();
		applyZoomInFadeInEffect(examContent);
		StuDashboardForm.root.setCenter(examContent);
	}

	private void applyZoomInFadeInEffect(AnchorPane content) {
		// Initially scale and make transparent
		content.setScaleX(0.5);
		content.setScaleY(0.5);
		content.setOpacity(0); // Initially invisible

		// Create a ScaleTransition for zoom-in effect
		ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), content);
		scaleTransition.setFromX(0.5);
		scaleTransition.setFromY(0.5);
		scaleTransition.setToX(1.0);
		scaleTransition.setToY(1.0);

		// Create a FadeTransition for fade-in effect
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), content);
		fadeTransition.setFromValue(0.0); // Start fully transparent
		fadeTransition.setToValue(1.0); // Fully visible

		// Play both transitions together
		scaleTransition.play();
		fadeTransition.play();
	}

	public int getReviewTotalQuestions() {
		return reviewTotalQuestions;
	}

	public void setReviewTotalQuestions(int reviewTotalQuestions) {
		this.reviewTotalQuestions = reviewTotalQuestions;
	}

}