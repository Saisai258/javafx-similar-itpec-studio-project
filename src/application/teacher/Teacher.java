package application.teacher;

import java.sql.Date;

/**
 * Teacher class represents the structure of the teacher table in the database.
 */
public class Teacher {
    private int id;
    private int ides;// Auto-incremented unique ID
    private String teacher_id; // Unique Teacher Identifier
    private String name; // Teacher's Full Name
    private String email; // Email Address
    private String password; // Account Password
    private String address; // Residential Address
    private String phone; // Contact Number
    private String gender; // Gender (e.g., Male/Female)
    private String specialism; // Teacher's Specialty
    private Date DOB; // Date of Birth
    private byte[] photo; // Profile Photo Blob
    private int size; // Optional size metadata for photo

    // Full Constructor: For complete teacher details including photo
    public Teacher(String teacher_id, String name, String email, String password, String address, String phone,
                   String gender, String specialism, Date DOB, byte[] photo, int size) {
        this.teacher_id = teacher_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.specialism = specialism;
        this.DOB = DOB;
        this.photo = photo;
        this.size = size;
    }

    // Constructor for updating teacher details
    public Teacher(int id, int ides2, String teacher_id, String name, String email, String phone, Date DOB, String address,
                   String gender, String tspecialism) {
        this.id = id;
        this.ides = ides2;
        this.teacher_id = teacher_id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.DOB = DOB;
        this.specialism = tspecialism;
    }

    public int getIdes() {
		return ides;
	}

	public void setIdes(int ides) {
		this.ides = ides;
	}

	// Partial Constructor: For basic teacher initialization (without photo/specialism)
    public Teacher(String teacher_id, String name, String email, String phone, String address, String gender, Date DOB) {
        this.teacher_id = teacher_id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.DOB = DOB;
    }

    // Getters and Setters for all properties
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSpecialism() {
        return specialism;
    }

    public void setSpecialism(String specialism) {
        this.specialism = specialism;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
