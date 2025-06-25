package bean;

import java.io.Serializable;

public class Teacher implements Serializable{

	private String id;
	private String name;
	private String password;
	private School school;

	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public School getSchool() {
		return school;
	}

	public void setId(String id) {
		this.id=id;
	}

	public void setName(String name) {
		this.name=name;
	}

	public void setPassword(String password) {
		this.password=password;
	}

	public void setSchool(School school) {
		this.school=school;
	}

}
