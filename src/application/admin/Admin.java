//package application.admin;
//
//import java.sql.Date;
//
//public class Admin {
//	private int id;
//	private String admin_id;
//	private String name;
//	private String email;
//	private String address;
//	private String phone;
//	private String gender;
//	private Date dateOfBirth;
//	private String password;
//	private byte[] photo;
//	private Integer size; // Nullable
//
//	// Constructor for all fields
//	public Admin(int id, String admin_id, String name, String email, String address, String phone, String gender,
//			Date dateOfBirth, String password, byte[] photo, Integer size) {
//		this.id = id;
//		this.admin_id = admin_id;
//		this.name = name;
//		this.email = email;
//		this.address = address;
//		this.phone = phone;
//		this.gender = gender;
//		this.dateOfBirth = dateOfBirth;
//		this.password = password;
//		this.photo = photo;
//		this.size = size;
//	}
//
//	// Constructor for required fields only
//	public Admin(String admin_id, String name, String email, String address, String phone, String gender,
//			String password, Date dateOfBirth) {
//		this.admin_id = admin_id;
//		this.name = name;
//		this.email = email;
//		this.address = address;
//		this.phone = phone;
//		this.gender = gender;
//		this.dateOfBirth = dateOfBirth;
//		this.password = password;
//	}
//
//	// Getters and Setters
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public String getAdminId() {
//		return admin_id;
//	}
//
//	public void setAdminId(String admin_id) {
//		this.admin_id = admin_id;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getAddress() {
//		return address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//	}
//
//	public String getPhone() {
//		return phone;
//	}
//
//	public void setPhone(String phone) {
//		this.phone = phone;
//	}
//
//	public String getGender() {
//		return gender;
//	}
//
//	public void setGender(String gender) {
//		this.gender = gender;
//	}
//
//	public Date getDateOfBirth() {
//		return dateOfBirth;
//	}
//
//	public void setDateOfBirth(Date dateOfBirth) {
//		this.dateOfBirth = dateOfBirth;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public byte[] getPhoto() {
//		return photo;
//	}
//
//	public void setPhoto(byte[] photo) {
//		this.photo = photo;
//	}
//
//	public Integer getSize() {
//		return size;
//	}
//
//	public void setSize(Integer size) {
//		this.size = size;
//	}
//}

package application.admin;

import java.sql.Date;

public class Admin {
	private int id;
	private String admin_id;
	private String name;
	private String email;
	private String address;
	private String phone;
	private String gender;
	private Date dateOfBirth;
	private String password;
	private byte[] photo;
	private Integer size; // Nullable

	// Constructor for all fields
	public Admin(int id, String admin_id, String name, String email, String address, String phone, String gender,
			Date dateOfBirth, String password, byte[] photo, Integer size) {
		this.id = id;
		this.admin_id = admin_id;
		this.name = name;
		this.email = email;
		this.address = address;
		this.phone = phone;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.password = password;
		this.photo = photo;
		this.size = size;
	}

//	// Constructor for required fields only
//	public Admin(String admin_id, String name, String email, String address, String phone, String gender,
//			String password, Date dateOfBirth) {
//		this.admin_id = admin_id;
//		this.name = name;
//		this.email = email;
//		this.address = address;
//		this.phone = phone;
//		this.gender = gender;
//		this.dateOfBirth = dateOfBirth;
//		this.password = password;
//	}

	//i change the password to front of address 
	public Admin(String admin_id, String name, String email,String password, String address, String phone, String gender,
			 Date dateOfBirth) {
		this.admin_id = admin_id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.address = address;
		this.phone = phone;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		
	}
	
	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAdminId() {
		return admin_id;
	}

	public void setAdminId(String admin_id) {
		this.admin_id = admin_id;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
}

