package application.admin;

import java.util.Date;

public class Contact {
	public int id;
	public String name;
	public String gmail;
	public String message;
	Date date;
	
	public Contact(int id2, String name2, String gmail2, Date date, String message2) {
		// TODO Auto-generated constructor stub
		this.id=id2;
		this.name=name2;
		this.gmail=gmail2;
		this.date=date;
		this.message=message2;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGmail() {
		return gmail;
	}

	public void setGmail(String gmail) {
		this.gmail = gmail;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
