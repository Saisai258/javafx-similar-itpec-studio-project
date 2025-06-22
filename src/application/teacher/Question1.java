package application.teacher;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class Question1 {
	public int qNo;
	private int ym_id;
	public String q_chapter;
	public String q_type;
	public String q_desc;
	public String ans_a;
	public String ans_b;
	public String ans_c;
	public String ans_d;
	public String q_ans;
	public byte[] q_descimg;
	public byte[] ans_aimg;
	public byte[] ans_bimg;
	public byte[] ans_cimg;
	public byte[] ans_dimg;
	public byte[] q_ansimg;
	public int q_id;
	
	//public Question(int upyear,String q_month,int upc_id,String q_desc,String upq_image,String ans_a,String ans_b,String ans_c,String ans_d,String q_ans) {}
	public Question1(int q_id,int ym_id, String q_chapter, String q_desc,byte[] imageData1, String answer_a,
			byte[] ansimageaData, String answer_b, byte[] ansimagebData, String answer_c, byte[] ansimagecData, String answer_d, byte[] ansimagedData, String q_ans, byte[] ansimageData) {
		this.q_id=q_id;
		this.ym_id=ym_id;
		this.q_chapter=q_chapter;
		this.q_desc=q_desc;
		this.q_descimg=imageData1;
		this.ans_a=answer_a;
		this.ans_aimg=ansimageaData;
		this.ans_b=answer_b;
		this.ans_bimg=ansimagebData;
		this.ans_c=answer_c;
		this.ans_cimg=ansimagecData;
		this.ans_d=answer_d;
		this.ans_dimg=ansimagedData;
		this.q_ans=q_ans;
		this.q_ansimg=ansimageData;
//		Question.check=check;
	}
	public Question1(TextField qid, ComboBox<String> updateyear, ComboBox<String> updatemonth,
			ComboBox<String> updatechapter, TextArea qdesc, ImageView descimage, TextArea qanswera, ImageView imga,
			TextArea qanswerb, ImageView imgb, TextArea qanswerc, ImageView imgc, TextArea qanswerd, ImageView imgd,
			TextArea correctans, ImageView correctimg) {
		// TODO Auto-generated constructor stub
	}
	public int getQ_num() {
		return qNo;
	}
	public void setQ_num(int q_num) {
		this.qNo = q_num;
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
	public void setAns_aimg(byte[] ans_aimg) {
		this.ans_aimg = ans_aimg;
	}
	public void setAns_bimg(byte[] ans_bimg) {
		this.ans_bimg = ans_bimg;
	}
	public void setAns_cimg(byte[] ans_cimg) {
		this.ans_cimg = ans_cimg;
	}
	public void setAns_dimg(byte[] ans_dimg) {
		this.ans_dimg = ans_dimg;
	}
	public void setQ_ansimg(byte[] q_ansimg) {
		this.q_ansimg = q_ansimg;
	}
	public byte[] getAns_aimg() {
		return ans_aimg;
	}
	public byte[] getAns_bimg() {
		return ans_bimg;
	}
	public byte[] getAns_cimg() {
		return ans_cimg;
	}
	public byte[] getAns_dimg() {
		return ans_dimg;
	}
	public byte[] getQ_ansimg() {
		return q_ansimg;
	}
	public int getYm_id() {
		return ym_id;
	}
	public void setYm_id(int ym_id) {
		this.ym_id = ym_id;
	}
}
