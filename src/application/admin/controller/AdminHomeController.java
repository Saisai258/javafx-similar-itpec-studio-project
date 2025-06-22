package application.admin.controller;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import application.main.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

public class AdminHomeController implements Initializable {
    @FXML
    private PieChart pieChart;
    @FXML
    private PieChart pieChart1;

    @FXML
    private void btnloadStorageData() {
        pieChart.getData().clear(); // Clear previous data
        ObservableList<PieChart.Data> pieData = getAdminTableStorage();

        if (!pieData.isEmpty()) {
            pieChart.setData(pieData);
        } else {
            System.out.println("No data found! Check database and table name.");
        }
    }

	private ObservableList<PieChart.Data> getAdminTableStorage() {
		ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
		String databaseName = "itpecoldquestionprojectdb";
		String tableName = "question";

		String query = "SELECT table_name, " +
                "ROUND((data_length + index_length) / 1024 / 1024, 2) AS `Total Size (MB)`, " + // Added missing comma here
                "ROUND(data_length / 1024 / 1024, 2) AS data_size_MB, " +
                "ROUND(index_length / 1024 / 1024, 2) AS index_size_MB " +
                "FROM information_schema.tables " +
                "WHERE table_schema = '" + databaseName + "' " +
                "AND table_name = '" + tableName + "'";

		try (Connection con = new DatabaseConnection().getConnetion();
				Statement statement = con.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			if (resultSet.next()) {
				double dataSize = resultSet.getDouble("data_size_MB");
				double totalSize = resultSet.getDouble("Total Size (MB)");
				data.add(new PieChart.Data("Used", dataSize));
				data.add(new PieChart.Data("Total", totalSize));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

    @FXML
    void btnDatabaseAction() {
        pieChart1.getData().clear(); // Clear previous data
        ObservableList<PieChart.Data> pieData1 = getDatabaseStorage();
        if (!pieData1.isEmpty()) {
            pieChart1.setData(pieData1);
        } else {
            System.out.println("No data found! Check database and table name.");
        }
    }

    private ObservableList<PieChart.Data> getDatabaseStorage() {
		ObservableList<PieChart.Data> data1 = FXCollections.observableArrayList();
		File file = new File("C:/"); // or the drive where the DB is stored
		long freeSpace = file.getFreeSpace(); // bytes
		long totalSpace = file.getTotalSpace(); // bytes
//		double maxInnoDBSizeMB = 131072;
		String dataDir = "C:\\ProgramData\\MySQL\\MySQL Server 9.1\\Data\\"; // adjust path
		String database = "itpecoldquestionprojectdb";
		String tableName = "question"; // your table name
		double useSizeMB = totalSpace - freeSpace;
		// InnoDB stores each table as an .ibd file if innodb_file_per_table = ON
		File tableFile = new File(dataDir + database + "/" + tableName + ".ibd");

		if (tableFile.exists()) {
			long fileSizeBytes = tableFile.length();
			double fileSizeMB = fileSizeBytes / (1024.0 * 1024.0);

			System.out.println("Used Storage of table '" + tableName + "': " + String.format("%.2f", fileSizeMB) + " MB");
//			System.out.println("Max InnoDB table size: " + maxInnoDBSizeMB + " MB");
		} else {
			System.out.println("Table file not found: " + tableFile.getAbsolutePath());
		}
		data1.add(new PieChart.Data("Used", useSizeMB));
		data1.add(new PieChart.Data("Maxi", totalSpace));
		data1.add(new PieChart.Data("Remain", freeSpace));



		return data1;
	}

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        btnloadStorageData();
        btnDatabaseAction();
    }
}
