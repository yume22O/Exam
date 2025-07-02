package bean;

import java.io.Serializable;

public class Subject implements Serializable{

	private String school; //学校コード
	private String cd; //科目コード
	private String name; //科目名

	public String getSchool() {
			return school;
	}
	public String getName() {
			return name;
	}

	public String getCd() {
			return cd;
	}


	public void setSchool(String school) {
			this.school=school;
	}

	public void setName(String name) {
			this.name=name;
	}

	public void setCd(String cd) {
			this.cd=cd;
	}



}