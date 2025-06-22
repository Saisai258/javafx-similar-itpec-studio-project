package application.admin.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import application.admin.Contact;
import application.main.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ContactListController implements Initializable{

	  @FXML
	    private TableView<Contact> contactList;

	    @FXML
	    private TableColumn<Contact, Integer> tNo;

	    @FXML
	    private TableColumn<Contact, String> tGmail;

	    @FXML
	    private TableColumn<Contact, String> tName;

	    @FXML
	    private TableColumn<Contact, Date> tDate;
	    
	    @FXML
	    private Label txtName;
	    @FXML
	    private TextArea txtMessage;
	    private ObservableList<Contact> list;
	    
	    private void showStudentList() throws SQLException  {
			// TODO Auto-generated method stub

	    	tNo.setCellValueFactory(new PropertyValueFactory<>("id"));
	    	tDate.setCellValueFactory(new PropertyValueFactory<>("date"));
			tName.setCellValueFactory(new PropertyValueFactory<>("name"));
			tGmail.setCellValueFactory(new PropertyValueFactory<>("gmail"));
			contactList.setItems(extractData());
		}
	    private ObservableList<Contact> extractData() throws SQLException {
	    	list = FXCollections.observableArrayList();
	    	DatabaseConnection db = new DatabaseConnection();
			Connection con = db.getConnetion();

			PreparedStatement pst = con.prepareStatement("select * from contact");
			
			ResultSet rs= pst.executeQuery();
			int i = 0;
			while (rs.next()) {
				i++;
				int id = i;
				String name = rs.getString("username");
				String gmail = rs.getString("usergmail");
				Date date = rs.getDate("date");
				String message = rs.getString("description");
				Contact contacatlist = new Contact(id,name,gmail,date,message);
				list.add(contacatlist);
			}
			return list;
			
	    }
	    @FXML
	    void onMouseClick(MouseEvent event) {

	    	Contact cont= contactList.getSelectionModel().getSelectedItem();
	    	if(cont!=null) {
	    		txtMessage.setText(cont.getMessage());
	    		txtName.setText(cont.getName());
	    	}else {
	    		Alert alert = new Alert(AlertType.WARNING);
	  		    alert.setTitle("NOT FOUND DATA!");
	  		    alert.setContentText("Please check your data.");
	  		    alert.show();
	    	}
	    }
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			try {
				showStudentList();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
