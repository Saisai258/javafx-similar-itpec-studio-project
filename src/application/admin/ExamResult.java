package application.admin;

import java.sql.Date;

public class ExamResult {

	private int e_id;
	private Date exam_date;
	private int question_number;
	private String exam_name;
	private String exam_type;
	private int mark;

	// Constructor for ip
	public ExamResult(int e_id,Date exam_date, int question_number, String exam_name, String exam_type, int smark) {
		this.e_id=e_id;
		this.exam_date = exam_date;
		this.question_number = question_number;
		this.exam_name = exam_name;
		this.exam_type = exam_type;
		this.mark = smark;
	}
	
	public int getE_id() {
		return e_id;
	}

	public void setE_id(int e_id) {
		this.e_id = e_id;
	}
	// Getters and Setters
	public Date getExam_date() {
		return exam_date;
	}

	public void setExam_date(Date exam_date) {
		this.exam_date = exam_date;
	}

	public int getQuestion_number() {
		return question_number;
	}

	public void setQuestion_number(int question_number) {
		this.question_number = question_number;
	}

	public String getExam_name() {
		return exam_name;
	}

	public void setExam_name(String exam_name) {
		this.exam_name = exam_name;
	}

	public String getExam_type() {
		return exam_type;
	}

	public void setExam_type(String exam_type) {
		this.exam_type = exam_type;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

}
