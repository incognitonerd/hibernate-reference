package com.sandbox.models.manytomanybidirectional;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "student")
public class Student {

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
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinTable(name = "course_student", 
	joinColumns = @JoinColumn(name = "student_id"), 
	inverseJoinColumns = @JoinColumn(name = "course_id"))
	private List<Course> courses;
	
	public Student() {
	}

	public Student(String fName, String lName, String email) {
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

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	public void addCourse(Course newCourse) {
		if (courses == null) {
			courses = new ArrayList<Course>();
		}
		courses.add(newCourse);
		//newCourse.addStudent(this);
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", fName=" + fName + ", lName=" + lName + ", email=" + email + "]";
	}
}
