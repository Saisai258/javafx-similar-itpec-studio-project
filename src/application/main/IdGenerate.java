 package application.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class IdGenerate {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		LocalDate date = LocalDate.now();
		int month = date.getMonthValue();
		String year = Integer.toString(date.getYear()%100);
		
		String formattedMonth = String.format("%02d", month);
        
		int id = idgenerate();
        String formattedid = String.format("%03d", id);
        System.out.println("Number: "+ formattedid);
        
        String realid ="MI-"+year + formattedMonth + formattedid;
        System.out.println("id: " + realid);
	}
	public static int idgenerate() throws SQLException, ClassNotFoundException {
		int rowcount = 0;
		Class.forName("com.mysql.cj.jdbc.Driver");
		DatabaseConnection db = new DatabaseConnection();
		Connection con = db.getConnetion();
		PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) AS total_rows FROM student;");
		ResultSet rs = pst.executeQuery();
		if(rs.next()) {
			rowcount = rs.getInt("total_rows");
		}
		int idgenerate = rowcount + 1;
		return idgenerate;	
	}
	
}
