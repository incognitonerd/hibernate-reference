package com.sandbox.models.onetoonebidirectional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;


@Entity
@Table(name = "instructor")
public class Instructor {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "first_name")
	private String fName;
	
	@Column(name = "last_name")
	private String lName;
	
	@Column(name = "email")
	private String email;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "instructor_detail_id")
	private InstructorDetail instructorDetail;
	
	public Instructor() {
	}

	public Instructor(String fName, String lName, String email) {
		this.fName = fName;
		this.lName = lName;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public InstructorDetail getInstructorDetail() {
		return instructorDetail;
	}

	public void setInstructorDetail(InstructorDetail instructorDetail) {
		this.instructorDetail = instructorDetail;
	}

	@Override
	public String toString() {
		return "Instructor [id=" + id + ", fName=" + fName + ", lName=" + lName + ", email=" + email
				+ ", instructorDetail=" + instructorDetail + "]";
	}
}
