package application.student.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.main.DatabaseConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

public class HistoryController implements Initializable {

	@FXML
	private TableView<ExamHistory> historyTable;
	@FXML
	private TableColumn<ExamHistory, Integer> no;
	@FXML
	private TableColumn<ExamHistory, String> dateTaken;
	@FXML
	private TableColumn<ExamHistory, String> examName;
	@FXML
	private TableColumn<ExamHistory, String> examType;
	@FXML
	private TableColumn<ExamHistory, Integer> totalQuestion;
	@FXML
	private TableColumn<ExamHistory, Integer> marksObtained;
	@FXML
	private TableColumn<ExamHistory, String> result;
	@FXML
	private PieChart pieChart;
	@FXML
	StackPane pieChartContainer;

	private String studentId = SignInController.login_student.getStudent_id();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			loadTableData();
			loadPieChartData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadTableData() throws SQLException {
		no.setCellValueFactory(new PropertyValueFactory<>("id"));
		examName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExamName()));
		dateTaken.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateTaken()));
		examType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExamType()));
		marksObtained.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getMarksObtained()).asObject());
		totalQuestion.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalQuestion()).asObject());
		result.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getResult()));

		result.setCellFactory(column -> new TableCell<ExamHistory, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setStyle("");
				} else {
					setText(item);
					if ("Pass".equals(item)) {
						setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold;");
					} else {
						setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
					}
				}
			}
		});

		historyTable.setItems(getResultListAll());
	}

	private void loadPieChartData() {
		int passCount = 0;
		int totalCount = historyTable.getItems().size();

		for (ExamHistory history : historyTable.getItems()) {
			if ("Pass".equals(history.getResult())) {
				passCount++;
			}
		}

		int failCount = totalCount - passCount;
		ObservableList<PieChart.Data> pieChartData = FXCollections
				.observableArrayList(new PieChart.Data("Pass", passCount), new PieChart.Data("Fail", failCount));

		pieChart.setData(pieChartData);
		for (PieChart.Data data : pieChart.getData()) {
			data.getNode()
					.setStyle("Pass".equals(data.getName()) ? "-fx-pie-color: #33ff33;" : "-fx-pie-color: #ff3333;");
		}

		updateLegendColors(pieChart);

		double percentage = (totalCount > 0) ? ((double) passCount / totalCount) * 100 : 0;
		Label centerLabel = new Label(String.format("%.1f%%", percentage));
		centerLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-text-fill: white;");

		// Clear previous children before adding new ones
		pieChartContainer.getChildren().clear();
		pieChartContainer.getChildren().addAll(pieChart, centerLabel);
	}

	private void updateLegendColors(PieChart pieChart) {
		for (Node node : pieChart.lookupAll(".chart-legend-item")) {
			if (node instanceof Label) {
				Label label = (Label) node;
				for (PieChart.Data data : pieChart.getData()) {
					if (label.getText().equals(data.getName())) {
						Node symbol = label.getGraphic();
						if (symbol != null) {
							if (data.getName().equals("Pass")) {
								symbol.setStyle("-fx-background-color: #33ff33;");
							} else if (data.getName().equals("Fail")) {
								symbol.setStyle("-fx-background-color: #ff3333;");
							}
						}
					}
				}
			}
		}
	}

	private ObservableList<ExamHistory> getResultListAll() throws SQLException {
		ObservableList<ExamHistory> list = FXCollections.observableArrayList();
		Connection con = new DatabaseConnection().getConnetion();
		String query = "SELECT exam_date, total_mark, question_number,exam_name,exam_type FROM examresult WHERE student_id=?";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1, studentId);
		ResultSet rs = pst.executeQuery();

		int i = 0;
		while (rs.next()) {
			i++;
			String dateTakenStr = rs.getDate("exam_date") != null ? rs.getDate("exam_date").toString() : "Unknown";
			int marksObtained = rs.getInt("total_mark");
			int totalQuestion = rs.getInt("question_number");
			String examName = rs.getString("exam_name");
			String examType = rs.getString("exam_type");
			String result = (totalQuestion > 0 && ((marksObtained * 100.0) / totalQuestion) >= 60) ? "Pass" : "Fail";
			list.add(new ExamHistory(i, examName, dateTakenStr, examType, totalQuestion, marksObtained, result));
		}

		rs.close();
		pst.close();
		con.close();
		return list;
	}

	public class ExamHistory {
		private int id;
		private final SimpleStringProperty examName;
		private final SimpleStringProperty dateTaken;
		private final SimpleStringProperty examType;
		private final SimpleIntegerProperty totalQuestion;
		private final SimpleIntegerProperty marksObtained;
		private final SimpleStringProperty result;

		public ExamHistory(int id, String examName, String dateTaken, String examType, int totalQuestion,
				int marksObtained, String result) {
			this.id = id;
			this.examName = new SimpleStringProperty(examName);
			this.dateTaken = new SimpleStringProperty(dateTaken);
			this.examType = new SimpleStringProperty(examType);
			this.totalQuestion = new SimpleIntegerProperty(totalQuestion);
			this.marksObtained = new SimpleIntegerProperty(marksObtained);
			this.result = new SimpleStringProperty(result);
		}

		public int getId() {
			return id;
		}

		public String getExamName() {
			return examName.get();
		}

		public String getDateTaken() {
			return dateTaken.get();
		}

		public String getExamType() {
			return examType.get();
		}

		public int getTotalQuestion() {
			return totalQuestion.get();
		}

		public int getMarksObtained() {
			return marksObtained.get();
		}

		public String getResult() {
			return result.get();
		}
	}
}
