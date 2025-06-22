package application.teacher;

public class Question {
	private int qNo;
	private int ym_id;
	private int img_id;
	private int c_id;
	private int t_id;
	private String q_chapter;
	private String q_type;
	private String q_desc;
	private String ans_a;
	private String ans_b;
	private String ans_c;
	private String ans_d;
	private String q_ans;
	private byte[] q_descimg;
	private byte[] ans_aimg;
	private byte[] ans_bimg;
	private byte[] ans_cimg;
	private byte[] ans_dimg;
	private byte[] q_ansimg;
	private int q_id;
	private int exam_id;
	private int esection_id;

	// Constructor that accepts text and answer data
	public Question(int ym_id, String q_chapter, String q_type, String q_desc, String answer_a,
			String answer_b, String answer_c, String answer_d, String q_ans) {
		this.ym_id = ym_id;
		this.q_chapter = q_chapter;
		this.q_type = q_type;
		this.q_desc = q_desc;
		this.ans_a = answer_a;
		this.ans_b = answer_b;
		this.ans_c = answer_c;
		this.ans_d = answer_d;
		this.q_ans = q_ans;
	}

	// Constructor that accepts image data
	public Question(int qid, int qNo2, String qDesc, String ansA, String ansB, String ansC, String ansD, String qAns, int ym_id2, int img_id2, int c_id2, int t_id2, byte[] q_descimg, int exam_id2, int esection_id2, byte[] ans_aimg, byte[] ans_bimg, byte[] ans_cimg, byte[] ans_dimg,
			byte[] q_ansimg) {
		this.q_id = qid;
		this.qNo = qNo2;
		this.q_desc = qDesc;
		this.ans_a = ansA;
		this.ans_b = ansB;
		this.ans_c = ansC;
		this.ans_d = ansD;
		this.q_ans = qAns;
		this.img_id =img_id2;
		this.c_id=c_id2;
		this.t_id=t_id2;
		this.exam_id=exam_id2;
		this.esection_id=esection_id2;
		this.ym_id = ym_id2;
		this.q_descimg = q_descimg;
		this.ans_aimg = ans_aimg;
		this.ans_bimg = ans_bimg;
		this.ans_cimg = ans_cimg;
		this.ans_dimg = ans_dimg;
		this.q_ansimg = q_ansimg;
	}

	// Combined constructor for both text and image data
	public Question(String q_chapter, String q_type, String q_desc, String answer_a,
			String answer_b, String answer_c, String answer_d, String q_ans, byte[] q_descimg, byte[] ans_aimg,
			byte[] ans_bimg, byte[] ans_cimg, byte[] ans_dimg, byte[] q_ansimg) {
		this.q_descimg = q_descimg;
		this.ans_aimg = ans_aimg;
		this.ans_bimg = ans_bimg;
		this.ans_cimg = ans_cimg;
		this.ans_dimg = ans_dimg;
		this.q_ansimg = q_ansimg;
	}

	public Question(int qId, String q_month, String q_chapter, String q_type, String q_desc, String ans_a, String ans_b,
			String ans_c, String ans_d, String q_ans, int q_year, int ymid, int imgId, int cid, int tid) {
// Setting the instance variables based on the provided constructor parameters
		this.q_id = qId; // Assuming 'qId' is intended to set the unique question ID
		this.q_chapter = q_chapter;
		this.q_type = q_type;
		this.q_desc = q_desc;
		this.ans_a = ans_a;
		this.ans_b = ans_b;
		this.ans_c = ans_c;
		this.ans_d = ans_d;
		this.q_ans = q_ans;// Assuming q_year is the year value for this question
// You can add additional parameters (ymid, imgId, cid, tid) depending on their purpose
	}

	public Question(String qDesc, String ansA, String ansB, String ansC, String ansD, String qAns, int ym_id,
			int img_Id,int exam_id, int c_id, int t_id, byte[] qImg, byte[] ansAImg, byte[] ansBImg, byte[] ansCImg,
			byte[] ansDImg, byte[] qAnsImg) {
		// TODO Auto-generated constructor stub
		this.q_desc = qDesc;
		this.ans_a = ansA;
		this.ans_b = ansB;
		this.ans_c = ansC;
		this.ans_d = ansD;
		this.q_ans = qAns;
		this.img_id =img_Id;
		this.c_id=c_id;
		this.t_id=t_id;
		this.exam_id=exam_id;
		this.ym_id = ym_id;
		this.q_descimg = qImg;
		this.ans_aimg = ansAImg;
		this.ans_bimg = ansBImg;
		this.ans_cimg = ansCImg;
		this.ans_dimg = ansDImg;
		this.q_ansimg = qAnsImg;
	}

	public Question(int q_id,int qNo,String qDesc, String ansA, String ansB, String ansC, String ansD, String qAns, int ym_id,
			int img_id,int exam_id, int c_id, int t_id, byte[] qImg, byte[] ansAImg, byte[] ansBImg, byte[] ansCImg,
			byte[] ansDImg, byte[] qAnsImg,int esection_id) {
		// TODO Auto-generated constructor stub
		this.q_id = q_id;
		this.qNo = qNo;
		this.q_desc = qDesc;
		this.ans_a = ansA;
		this.ans_b = ansB;
		this.ans_c = ansC;
		this.ans_d = ansD;
		this.q_ans = qAns;
		this.img_id =img_id;
		this.c_id=c_id;
		this.exam_id=exam_id;
		this.t_id=t_id;
		this.ym_id = ym_id;
		this.q_descimg = qImg;
		this.ans_aimg = ansAImg;
		this.ans_bimg = ansBImg;
		this.ans_cimg = ansCImg;
		this.ans_dimg = ansDImg;
		this.q_ansimg = qAnsImg;
		this.esection_id = esection_id;
	}

	public Question(int qNo2, int c_id2) {
		// TODO Auto-generated constructor stub
		this.qNo = qNo2;
		this.c_id=c_id2;
	}

	public String getQ_chapter() {
		return q_chapter;
	}

	public void setQ_chapter(String q_chapter) {
		this.q_chapter = q_chapter;
	}

	public String getQ_type() {
		return q_type;
	}

	public void setQ_type(String q_type) {
		this.q_type = q_type;
	}

	public String getQ_desc() {
		return q_desc;
	}

	public void setQ_desc(String q_desc) {
		this.q_desc = q_desc;
	}

	public String getAns_a() {
		return ans_a;
	}

	public void setAns_a(String ans_a) {
		this.ans_a = ans_a;
	}

	public String getAns_b() {
		return ans_b;
	}

	public void setAns_b(String ans_b) {
		this.ans_b = ans_b;
	}

	public String getAns_c() {
		return ans_c;
	}

	public void setAns_c(String ans_c) {
		this.ans_c = ans_c;
	}

	public String getAns_d() {
		return ans_d;
	}

	public void setAns_d(String ans_d) {
		this.ans_d = ans_d;
	}

	public String getQ_ans() {
		return q_ans;
	}

	public void setQ_ans(String q_ans) {
		this.q_ans = q_ans;
	}

	public byte[] getQ_descimg() {
		return q_descimg;
	}

	public void setQ_descimg(byte[] q_descimg) {
		this.q_descimg = q_descimg;
	}

	public int getQ_id() {
		return q_id;
	}

	public void setQ_id(int q_id) {
		this.q_id = q_id;
	}

	public byte[] getAns_aimg() {
		return ans_aimg;
	}

	public void setAns_aimg(byte[] ans_aimg) {
		this.ans_aimg = ans_aimg;
	}

	public byte[] getAns_bimg() {
		return ans_bimg;
	}

	public void setAns_bimg(byte[] ans_bimg) {
		this.ans_bimg = ans_bimg;
	}

	public byte[] getAns_cimg() {
		return ans_cimg;
	}

	public void setAns_cimg(byte[] ans_cimg) {
		this.ans_cimg = ans_cimg;
	}

	public byte[] getAns_dimg() {
		return ans_dimg;
	}

	public void setAns_dimg(byte[] ans_dimg) {
		this.ans_dimg = ans_dimg;
	}

	public byte[] getQ_ansimg() {
		return q_ansimg;
	}

	public void setQ_ansimg(byte[] q_ansimg) {
		this.q_ansimg = q_ansimg;
	}

	public int getYm_id() {
		return ym_id;
	}

	public void setYm_id(int ym_id) {
		this.ym_id = ym_id;
	}

	public int getImg_id() {
		return img_id;
	}

	public void setImg_id(int img_id) {
		this.img_id = img_id;
	}

	public int getC_id() {
		return c_id;
	}

	public void setC_id(int c_id) {
		this.c_id = c_id;
	}

	public int getT_id() {
		return t_id;
	}

	public void setT_id(int t_id) {
		this.t_id = t_id;
	}

	public int getQNo() {
	    return qNo;
	}

	public void setQNo(int qNo) {
		this.qNo = qNo;
	}

	public void add(Question q) {
		// TODO Auto-generated method stub
		
	}

	public int getExam_id() {
		return exam_id;
	}

	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}

	public int getEsection_id() {
		return esection_id;
	}

	public void setEsection_id(int esection_id) {
		this.esection_id = esection_id;
	}
}
