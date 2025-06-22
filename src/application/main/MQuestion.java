package application.main;

public class MQuestion {

	private int qid;
	private String clas;
	private String batch;
	private String etime;
	private String edate;
	private String qnum;
	private String qmark;
	private String qdesc;
	private String qchapter;
	private int year;

	public MQuestion(int id, String qClass, String qExamTime, String qExamDate, String qNumber,String qBatch,String qMarks,String qdesc,String qchapter) {
		// TODO Auto-generated constructor stub
	this.qid = id;
	this.batch = qBatch;
	this.clas = qClass;
	this.etime = qExamTime;
	this.edate = qExamDate;
	this.qnum = qNumber;
	this.qmark = qMarks;
	this.qdesc = qdesc;
	this.qchapter = qchapter;
		
	}

	public MQuestion(int year) {
		// TODO Auto-generated constructor stub
		this.year=year;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getQchapter() {
		return qchapter;
	}

	public void setQchapter(String qchapter) {
		this.qchapter = qchapter;
	}

	public int getQid() {
		return qid;
	}


	public void setQid(int qid) {
		this.qid = qid;
	}


	public String getClas() {
		return clas;
	}


	public void setClas(String clas) {
		this.clas = clas;
	}


	public String getBatch() {
		return batch;
	}


	public void setBatch(String batch) {
		this.batch = batch;
	}


	public String getEtime() {
		return etime;
	}


	public void setEtime(String etime) {
		this.etime = etime;
	}


	public String getEdate() {
		return edate;
	}


	public void setEdate(String edate) {
		this.edate = edate;
	}


	public String getQnum() {
		return qnum;
	}


	public void setQnum(String qnum) {
		this.qnum = qnum;
	}


	public String getQmark() {
		return qmark;
	}


	public void setQmark(String qmark) {
		this.qmark = qmark;
	}


	public String getQdesc() {
		return qdesc;
	}


	public void setQdesc(String qdesc) {
		this.qdesc = qdesc;
	}

	
}
