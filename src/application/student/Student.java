package application.student;

import java.sql.Date;

public class Student {
	private int id;
	private String student_id;
	private String name;
	private String email;
	private String password;
	private String address;
	private String phone;
	private String sclass;
	private String batch;
	private String gender;
	private Date DOB;
	private byte[] photo;
	private int psize;

	public Student(int id,String student_id, String name, String email, String sclass, String batch, String address,
			String phone, String gender, Date DOB, String password, byte[] photo, int psize) {
		this.id = id;
		this.student_id = student_id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.address = address;
		this.phone = phone;
		this.sclass = sclass;
		this.batch = batch;
		this.gender = gender;
		this.DOB = DOB;
		this.photo = photo;
		this.psize = psize;
	}

	public Student(int id, String sID, String sName, String sEmail, String sPhone, Date sDOB, String sAddress,
			String sGender, String sBatch, String sClass2) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.student_id = sID;
		this.name = sName;
		this.email = sEmail;
		this.address = sAddress;
		this.phone = sPhone;
		this.gender = sGender;
		this.DOB = sDOB;
		this.batch = sBatch;
		this.sclass =sClass2;
	}

	public Student(int id2, String sid, String sname, String sbatch, String sclass2) {
		// TODO Auto-generated constructor stub
		this.id = id2;
		this.student_id = sid;
		this.name = sname;
		this.sclass = sclass2;
		this.batch = sbatch;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getSclass() {
		return sclass;
	}

	public void setSclass(String sclass) {
		this.sclass = sclass;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDOB() {
		return DOB;
	}

	public void setDOB(Date dOB) {
		DOB = dOB;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public int getPsize() {
		return psize;
	}

	public void setPsize(int psize) {
		this.psize = psize;
	}

}
